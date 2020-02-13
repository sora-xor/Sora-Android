package jp.co.soramitsu.feature_project_impl.data.mappers

import jp.co.soramitsu.core_db.model.DiscussionLinkLocal
import jp.co.soramitsu.feature_project_api.domain.model.DiscussionLink
import jp.co.soramitsu.feature_project_impl.data.network.model.DiscussionLinkRemote
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DiscussionLinkMapperTest {

    private val discussionLinkRemote = DiscussionLinkRemote(
        "title",
        "link"
    )
    private val discussionLink = DiscussionLink(
        "title",
        "link"
    )
    private val discussionLinkLocal = DiscussionLinkLocal(
        "title",
        "link"
    )

    @Test
    fun `map discussion link remote to discussion link called`() {
        assertEquals(discussionLink, mapDiscussionLinkRemoteToDiscussionLink(discussionLinkRemote))
    }

    @Test
    fun `map discussion link local to discussion link called`() {
        assertEquals(discussionLink, mapDiscussionLinkLocalToDiscussionLink(discussionLinkLocal))
    }

    @Test
    fun `map discussion link to discussion link local called`() {
        assertEquals(discussionLinkLocal, mapDiscussionLinkToDiscussionLinkLocal(discussionLink))
    }
}