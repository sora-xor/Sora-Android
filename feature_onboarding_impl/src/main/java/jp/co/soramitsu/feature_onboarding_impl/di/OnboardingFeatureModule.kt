/**
* Copyright Soramitsu Co., Ltd. All Rights Reserved.
* SPDX-License-Identifier: GPL-3.0
*/

package jp.co.soramitsu.feature_onboarding_impl.di

import dagger.Module
import dagger.Provides
import jp.co.soramitsu.feature_onboarding_api.OnboardingStarter
import jp.co.soramitsu.feature_onboarding_impl.OnboardingStarterImpl

@Module
class OnboardingFeatureModule {

    @Provides
    fun provideOnboardingStarter(starter: OnboardingStarterImpl): OnboardingStarter = starter
}
