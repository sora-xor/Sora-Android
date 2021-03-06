/**
* Copyright Soramitsu Co., Ltd. All Rights Reserved.
* SPDX-License-Identifier: GPL-3.0
*/

package jp.co.soramitsu.feature_sse_impl.data.network.model

abstract class EventRemote {

    abstract fun getEventType(): Type

    enum class Type {
        OperationStarted,
        EthRegistrationStarted,
        EthRegistrationCompleted,
        EthRegistrationFailed,
        OperationCompleted,
        OperationFailed,
        DepositOperationCompleted
    }
}