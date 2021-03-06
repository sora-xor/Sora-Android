/**
* Copyright Soramitsu Co., Ltd. All Rights Reserved.
* SPDX-License-Identifier: GPL-3.0
*/

package jp.co.soramitsu.feature_wallet_api.domain.interfaces

import io.reactivex.Single
import java.security.KeyPair

interface AccountSettings {

    fun getKeyPair(): Single<KeyPair>

    fun getAccountId(): Single<String>

    fun mnemonic(): Single<String>
}