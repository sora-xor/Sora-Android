/**
* Copyright Soramitsu Co., Ltd. All Rights Reserved.
* SPDX-License-Identifier: GPL-3.0
*/

package jp.co.soramitsu.feature_ethereum_api

interface EthStatusPollingServiceStarter {

    fun startEthStatusPollingServiceService()

    fun stopEthStatusPollingServiceService()
}