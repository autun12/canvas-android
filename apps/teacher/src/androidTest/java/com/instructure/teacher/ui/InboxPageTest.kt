/*
 * Copyright (C) 2017 - present Instructure, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */

package com.instructure.teacher.ui

import com.instructure.canvas.espresso.mockCanvas.MockCanvas
import com.instructure.canvas.espresso.mockCanvas.addConversations
import com.instructure.canvas.espresso.mockCanvas.init
import com.instructure.teacher.ui.utils.TeacherTest
import com.instructure.teacher.ui.utils.clickInboxTab
import com.instructure.teacher.ui.utils.tokenLogin
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class InboxPageTest: TeacherTest() {
    @Test
    override fun displaysPageObjects() {
        val data = MockCanvas.init(
                teacherCount = 1,
                courseCount = 1,
                favoriteCourseCount = 1
        )
        val teacher = data.teachers[0]
        data.addConversations(userId = teacher.id)
        val token = data.tokenFor(teacher)!!
        tokenLogin(data.domain, token, teacher)
        dashboardPage.clickInboxTab()
        inboxPage.assertPageObjects()
    }

    @Test
    fun displaysConversation() {
        val data = MockCanvas.init(
                courseCount = 1,
                favoriteCourseCount = 1,
                teacherCount = 1,
                studentCount = 1
        )
        val teacher = data.teachers[0]
        data.addConversations(userId = teacher.id)

        // Test expects single conversation; filter down to starred conversation
        val unwanted = data.conversations.filter() {entry -> !entry.value.isStarred}
        unwanted.forEach() { (id, conversation) -> data.conversations.remove(id)}

        val token = data.tokenFor(teacher)!!
        tokenLogin(data.domain, token, teacher)
        dashboardPage.clickInboxTab()
        inboxPage.assertHasConversation()
    }
}
