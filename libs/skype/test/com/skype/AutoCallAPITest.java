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

import com.skype.Call.DTMF;
import com.skype.connector.test.TestCaseByCSVFile;

public class AutoCallAPITest extends TestCaseByCSVFile {
    @Override
    protected void setUp() throws Exception {
        setRecordingMode(false);
    }
    
//    public void testConferenceId() throws Exception {
//        Call call = Skype.call("bitman", "jessy");
//        String result = call.getConferenceId();
//        if (isRecordingMode()) {
//            System.out.println(result);
//        } else {
//            assertEquals("11676", result);
//        }
//    }
    
    public void testSendDTMF() throws Exception {
        Thread.sleep(2000);
        Call call = Skype.call("echo123");
        for (DTMF command: DTMF.values()) {
            call.send(command);
        }
        call.finish();
    }
}
