package jp.co.soramitsu.feature_wallet_impl.data.network.request.extrinsic

import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest

class UnwatchExtrinsicRequest(subscriptionId: String) : RuntimeRequest(
    method = "author_unwatchExtrinsic",
    params = listOf(subscriptionId)
)
