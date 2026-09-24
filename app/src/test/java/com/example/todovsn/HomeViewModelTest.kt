package com.example.todovsn

import com.example.todovsn.data.Category
import com.example.todovsn.data.ToDoItem
import com.example.todovsn.data.ToDoRepository
import com.example.todovsn.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

/** In-memory fake, records calls for verification. */
private class FakeToDoRepository(initial: List<ToDoItem> = emptyList()) : ToDoRepository {
    private val items = MutableStateFlow(initial)
    val deleted = mutableListOf<ToDoItem>()
    val inserted = mutableListOf<ToDoItem>()
    val updated = mutableListOf<ToDoItem>()

    override fun getAllToDoStream(): Flow<List<ToDoItem>> = items.asStateFlow()
    override fun getToDoStream(id: Int): Flow<ToDoItem?> =
        MutableStateFlow(items.value.find { it.id == id })

    override suspend fun insertToDo(item: ToDoItem) {
        inserted += item
        items.update { it + item }
    }

    override suspend fun deleteToDo(item: ToDoItem) {
        deleted += item
        items.update { list -> list.filterNot { it.id == item.id } }
    }

    override suspend fun updateToDo(item: ToDoItem) {
        updated += item
        items.update { list -> list.map { if (it.id == item.id) item else it } }
    }

    override suspend fun deleteAllToDo() {
        items.value = emptyList()
    }

    override fun getAllCategoriesStream(): Flow<List<Category>> =
        MutableStateFlow(emptyList())

    override suspend fun insertCategory(category: Category) = Unit
    override suspend fun deleteCategory(category: Category) = Unit
    override suspend fun deleteCategoryAndMoveTasks(category: Category) = Unit
}

/**
 * QA: [HomeViewModel] delete / undo-restore / toggle-complete behavior.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun sample(id: Int, completed: Boolean = false) = ToDoItem(
        id = id,
        title = "Task $id",
        description = "Desc $id",
        category = "Personal",
        createdAt = LocalDateTime.of(2026, 9, 23, 10, 0),
        isCompleted = completed
    )

    @Test
    fun delete_removesItemFromStream() = runTest(dispatcher) {
        val repo = FakeToDoRepository(listOf(sample(1), sample(2)))
        val vm = HomeViewModel(repo)
        advanceUntilIdle()
        assertEquals(2, vm.homeUiState.value.toDoList.size)

        vm.deleteToDo(sample(1))
        advanceUntilIdle()

        assertEquals(listOf(2), vm.homeUiState.value.toDoList.map { it.id })
        assertEquals(listOf(sample(1)), repo.deleted)
    }

    @Test
    fun restore_reinsertsLastDeleted() = runTest(dispatcher) {
        val repo = FakeToDoRepository(listOf(sample(1)))
        val vm = HomeViewModel(repo)
        advanceUntilIdle()

        vm.deleteToDo(sample(1))
        advanceUntilIdle()
        assertTrue(vm.homeUiState.value.toDoList.isEmpty())

        vm.restoreDeletedToDo()
        advanceUntilIdle()

        assertEquals(1, vm.homeUiState.value.toDoList.size)
        assertEquals(sample(1), repo.inserted.single())
    }

    @Test
    fun toggleCompleted_flipsFlag() = runTest(dispatcher) {
        val repo = FakeToDoRepository(listOf(sample(1, completed = false)))
        val vm = HomeViewModel(repo)
        advanceUntilIdle()

        vm.toggleCompleted(sample(1, completed = false))
        advanceUntilIdle()

        val updated = repo.updated.single()
        assertTrue(updated.isCompleted)
        assertTrue(vm.homeUiState.value.toDoList.single().isCompleted)
    }

    @Test
    fun toggleCompleted_twice_returnsToOriginal() = runTest(dispatcher) {
        val repo = FakeToDoRepository(listOf(sample(1, completed = false)))
        val vm = HomeViewModel(repo)
        advanceUntilIdle()

        vm.toggleCompleted(vm.homeUiState.value.toDoList.single())
        advanceUntilIdle()
        vm.toggleCompleted(vm.homeUiState.value.toDoList.single())
        advanceUntilIdle()

        assertFalse(vm.homeUiState.value.toDoList.single().isCompleted)
    }
}
