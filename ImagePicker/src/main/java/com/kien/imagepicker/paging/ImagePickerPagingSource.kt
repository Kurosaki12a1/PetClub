package com.kien.imagepicker.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kien.imagepicker.data.entity.Photo
import javax.inject.Inject
import kotlin.math.min

class ImagePickerPagingSource @Inject constructor(
    private val uris: ArrayList<Photo>
) : PagingSource<Int, Photo>() {

    companion object {
        const val PAGE_SIZE = 15
        private const val INITIAL_PAGE_INDEX = 0
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int?  {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(PAGE_SIZE) ?: page?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val start = params.key ?: INITIAL_PAGE_INDEX
            val end = min(start + params.loadSize, uris.size)
            val data = uris.subList(start, end)
            LoadResult.Page(
                data = data,
                prevKey = if (start == INITIAL_PAGE_INDEX) null else start - PAGE_SIZE,
                nextKey = if (end >= uris.size) null else end
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}