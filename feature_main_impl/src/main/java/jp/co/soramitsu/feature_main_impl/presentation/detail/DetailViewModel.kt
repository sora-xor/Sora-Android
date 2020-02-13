/**
* Copyright Soramitsu Co., Ltd. All Rights Reserved.
* SPDX-License-Identifier: GPL-3.0
*/

package jp.co.soramitsu.feature_main_impl.presentation.detail

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.co.soramitsu.common.interfaces.WithPreloader
import jp.co.soramitsu.common.presentation.viewmodel.BaseViewModel
import jp.co.soramitsu.common.resourses.ResourceManager
import jp.co.soramitsu.common.util.Event
import jp.co.soramitsu.common.util.NumbersFormatter
import jp.co.soramitsu.feature_main_api.launcher.MainRouter
import jp.co.soramitsu.feature_main_impl.R
import jp.co.soramitsu.feature_main_impl.domain.MainInteractor
import jp.co.soramitsu.feature_main_impl.presentation.detail.gallery.GalleryActivity
import jp.co.soramitsu.feature_project_api.domain.model.GalleryItem
import jp.co.soramitsu.feature_project_api.domain.model.GalleryItemType
import jp.co.soramitsu.feature_project_api.domain.model.ProjectDetails
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class DetailViewModel(
    private val interactor: MainInteractor,
    private val preloader: WithPreloader,
    private val router: MainRouter,
    private val projectId: String,
    private val numbersFormatter: NumbersFormatter,
    private val resourceManager: ResourceManager
) : BaseViewModel(), WithPreloader by preloader {

    companion object {
        private const val PROJECT_CHANGE_FAV_DELAY = 500L
    }

    val projectDetailsLiveData = MutableLiveData<ProjectDetails>()
    val playVideoLiveData = MutableLiveData<Event<String>>()
    val sendEmailEvent = MutableLiveData<Event<String>>()

    private val _showVoteUserLiveData = MutableLiveData<Event<Int>>()
    val showVoteUserLiveData: LiveData<Event<Int>> = _showVoteUserLiveData

    private val _showVoteProjectLiveData = MutableLiveData<Event<Int>>()
    val showVoteProjectLiveData: LiveData<Event<Int>> = _showVoteProjectLiveData

    private val _friendsVotedLiveData = MediatorLiveData<String>()
    val friendsVotedLiveData: LiveData<String> = _friendsVotedLiveData

    private val _favoritesCountLiveData = MediatorLiveData<String>()
    val favoritesLiveData: LiveData<String> = _favoritesCountLiveData

    private val _votesAndFavoritesVisibility = MediatorLiveData<Boolean>()
    val votesAndFavoritesVisibility: LiveData<Boolean> = _votesAndFavoritesVisibility

    private val _projectDescriptionLiveData = MediatorLiveData<String>()
    val projectDescriptionLiveData: LiveData<String> = _projectDescriptionLiveData

    private val _galleryLiveData = MediatorLiveData<List<GalleryItem>>()
    val galleryLiveData: LiveData<List<GalleryItem>> = _galleryLiveData

    private val _votesFormattedLiveData = MediatorLiveData<String>()
    val votesFormattedLiveData: LiveData<String> = _votesFormattedLiveData

    private val _showBrowserLiveData = MutableLiveData<Event<String>>()
    val showBrowserLiveData: LiveData<Event<String>> = _showBrowserLiveData

    private val votesLiveData = MutableLiveData<BigDecimal>()

    init {
        _friendsVotedLiveData.addSource(projectDetailsLiveData) { project ->
            val friendsVotedStr = if (project.votedFriendsCount == 0) {
                ""
            } else {
                val quantityString = resourceManager.getQuantityString(R.plurals.project_friends_template, project.votedFriendsCount)
                quantityString.format(project.votedFriendsCount.toString())
            }

            _friendsVotedLiveData.value = friendsVotedStr
        }

        _favoritesCountLiveData.addSource(projectDetailsLiveData) { project ->
            val favoritesCountStr = if (project.favoriteCount == 0) {
                ""
            } else {
                val quantityString = resourceManager.getQuantityString(R.plurals.project_details_favorites_format, project.favoriteCount)
                quantityString.format(project.favoriteCount.toString())
            }

            _favoritesCountLiveData.value = favoritesCountStr
        }

        _projectDescriptionLiveData.addSource(projectDetailsLiveData) {
            _projectDescriptionLiveData.value = if (it.detailedDescription.isEmpty()) {
                it.description
            } else {
                it.detailedDescription
            }
        }

        _votesAndFavoritesVisibility.addSource(projectDetailsLiveData) {
            _votesAndFavoritesVisibility.value = it.favoriteCount != 0 || it.votedFriendsCount != 0
        }

        _galleryLiveData.addSource(projectDetailsLiveData) {
            _galleryLiveData.value = it.gallery
        }

        _votesFormattedLiveData.addSource(votesLiveData) {
            val votes = numbersFormatter.formatInteger(it)
            _votesFormattedLiveData.value = if (votes.length > 4) {
                resourceManager.getString(R.string.project_votes_k_template).format(votes.substring(0, votes.length - 3).trim())
            } else {
                votes
            }
        }

        disposables.add(
            interactor.getProjectById(projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showPreloader() }
                .subscribe({
                    hidePreloader()
                    projectDetailsLiveData.value = it
                }, {
                    logException(it)
                })
        )
    }

    fun updateProject() {
        disposables.add(
            interactor.updateProject(projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {
                    logException(it)
                })
        )
    }

    fun backPressed() {
        router.popBackStack()
    }

    fun getVotes(updateCached: Boolean) {
        disposables.add(
            interactor.getVotes(updateCached)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!updateCached) getVotes(true)
                    votesLiveData.value = it
                }, {
                    logException(it)
                })
        )
    }

    private fun removeProjectFromFavorites() {
        disposables.add(
            interactor.removeProjectFromFavorites(projectId)
                .andThen(Completable.timer(PROJECT_CHANGE_FAV_DELAY, TimeUnit.MILLISECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateProject()
                }, {
                    onError(it)
                })
        )
    }

    private fun addProjectToFavorites() {
        disposables.add(
            interactor.addProjectToFavorites(projectId)
                .andThen(Completable.timer(PROJECT_CHANGE_FAV_DELAY, TimeUnit.MILLISECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateProject()
                }, {
                    onError(it)
                })
        )
    }

    fun voteForProject(votes: Long) {
        disposables.add(
            interactor.voteForProject(projectId, votes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateProject()
                    getVotes(true)
                }, {
                    onError(it)
                })
        )
    }

    fun websiteClicked() {
        projectDetailsLiveData.value?.projectLink?.let {
            _showBrowserLiveData.value = Event(it.toString())
        }
    }

    fun emailClicked() {
        projectDetailsLiveData.value?.email?.let {
            if (it.isNotEmpty()) {
                sendEmailEvent.value = Event(it)
            }
        }
    }

    fun galleryClicked(activity: Activity, galleryItemVm: GalleryItem, index: Int) {
        if (GalleryItemType.VIDEO == galleryItemVm.type) {
            playVideoLiveData.value = Event(galleryItemVm.url)
        } else {
            projectDetailsLiveData.value?.let { GalleryActivity.start(activity, it.gallery, index) }
        }
    }

    fun voteClicked() {
        projectDetailsLiveData.value?.let { project ->
            votesLiveData.value?.let { votes ->
                val votesLeft = (project.fundingTarget - project.fundingCurrent).toInt()
                if (votes.toInt() < votesLeft) {
                    _showVoteUserLiveData.value = Event(votes.toInt())
                } else {
                    _showVoteProjectLiveData.value = Event(votesLeft)
                }
            }
        }
    }

    fun favoriteClicked() {
        projectDetailsLiveData.value?.let { project ->
            if (project.isFavorite) {
                removeProjectFromFavorites()
            } else {
                addProjectToFavorites()
            }
        }
    }

    fun votesClicked() {
        router.showVotesHistory()
    }

    fun discussionLinkClicked() {
        projectDetailsLiveData.value?.discussionLink?.link?.let {
            _showBrowserLiveData.value = Event(it)
        }
    }
}