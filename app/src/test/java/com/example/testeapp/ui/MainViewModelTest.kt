package com.example.testeapp.ui

import com.example.testeapp.domain.PostsManager
import com.example.testeapp.model.Post
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import com.example.testeapp.model.Result
import com.example.testeapp.model.Error

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest{

    @RelaxedMockK
    private lateinit var postsManager: PostsManager

    @RelaxedMockK
    private lateinit var mockPostList: List<Post>

    private lateinit var dispatcher: TestDispatcher

    private lateinit var MainViewModel: MainViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        mockkStatic(Dispatchers::class)
        every {
            Dispatchers.Default
        } returns dispatcher

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when list is fetch successfully, then viewModel should contain the list and should not be in progress and error should be null`() =
        runTest {

            coEvery {
                postsManager.getPosts()
            } returns flow { emit(Result.success(mockPostList)) }

            MainViewModel = MainViewModel(postsManager)

            advanceUntilIdle()

            MainViewModel.state.value.apply {
                Assert.assertEquals(
                    postList,
                    mockPostList
                )

                Assert.assertEquals(
                    inProgress,
                    false
                )

                Assert.assertEquals(
                    errorMessage,
                    null
                )
            }

        }

    @Test
    fun `when list is  not fetch successfully, then viewModel should contain error and should not be in progress and list should be null`() =
        runTest {

            coEvery {
                postsManager.getPosts()
            } returns flow { emit(Result.error("any message", Error(404, "any message"))) }

            MainViewModel = MainViewModel(postsManager)

            advanceUntilIdle()

            MainViewModel.state.value.apply {
                Assert.assertEquals(
                    postList,
                    null
                )

                Assert.assertEquals(
                    inProgress,
                    false
                )

                Assert.assertEquals(
                    errorMessage,
                    "any message"
                )
            }

        }

    @Test
    fun `when fetch is in progress, then viewModel inprogress should be true and should contain both error and list as null`() =
        runTest {

            coEvery {
                postsManager.getPosts()
            } returns flow { emit(Result.inProgress()) }

            MainViewModel = MainViewModel(postsManager)

            advanceUntilIdle()

            MainViewModel.state.value.apply {
                Assert.assertEquals(
                    postList,
                    null
                )

                Assert.assertEquals(
                    inProgress,
                    true
                )

                Assert.assertEquals(
                    errorMessage,
                    null
                )
            }

        }

}