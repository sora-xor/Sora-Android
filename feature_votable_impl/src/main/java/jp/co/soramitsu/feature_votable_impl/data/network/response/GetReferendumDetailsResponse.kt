/**
* Copyright Soramitsu Co., Ltd. All Rights Reserved.
* SPDX-License-Identifier: GPL-3.0
*/

package jp.co.soramitsu.feature_votable_impl.data.network.response

import jp.co.soramitsu.common.data.network.dto.StatusDto

class GetReferendumDetailsResponse(
    val referendum: jp.co.soramitsu.feature_votable_impl.data.network.model.ReferendumRemote,
    val status: StatusDto
)