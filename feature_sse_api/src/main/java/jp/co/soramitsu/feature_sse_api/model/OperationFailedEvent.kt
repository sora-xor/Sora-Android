/**
* Copyright Soramitsu Co., Ltd. All Rights Reserved.
* SPDX-License-Identifier: GPL-3.0
*/

package jp.co.soramitsu.feature_sse_api.model

data class OperationFailedEvent(
    val timestamp: Long,
    val operationId: String,
    val reason: String?
) : Event() {

    override fun getEventType(): Type {
        return Type.OPERATION_FAILED
    }
}