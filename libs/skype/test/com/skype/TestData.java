/*******************************************************************************
 * Copyright (c) 2006-2007 Koji Hisano <hisano@gmail.com> - UBION Inc. Developer
 * Copyright (c) 2006-2007 UBION Inc. <http://www.ubion.co.jp/>
 * 
 * Copyright (c) 2006-2007 Skype Technologies S.A. <http://www.skype.com/>
 * 
 * Skype4Java is licensed under either the Apache License, Version 2.0 or
 * the Eclipse Public License v1.0.
 * You may use it freely in commercial and non-commercial products.
 * You may obtain a copy of the licenses at
 *
 *   the Apache License - http://www.apache.org/licenses/LICENSE-2.0
 *   the Eclipse Public License - http://www.eclipse.org/legal/epl-v10.html
 *
 * If it is possible to cooperate with the publicity of Skype4Java, please add
 * links to the Skype4Java web site <https://developer.skype.com/wiki/Java_API> 
 * in your web site or documents.
 * 
 * Contributors:
 * Koji Hisano - initial API and implementation
 ******************************************************************************/
package com.skype;

import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;

final class TestData {
    private static TestCaseProperties data = new TestCaseProperties(TestData.class);

    static Friend getFriend() throws SkypeException {
        return Skype.getContactList().getFriend(data.getProperty("id"));
    }

    static Friend getFriend2() throws SkypeException {
        return Skype.getContactList().getFriend(data.getProperty("id2"));
    }

    static String getFriendId() throws SkypeException {
        return getFriend().getId();
    }

    static String getFriendDisplayName() throws SkypeException {
        return getFriend().getDisplayName();
    }

    static String getSMSNumber() throws SkypeException {
        return data.getProperty("smsNumber");
    }

    private TestData() {
    }
}
