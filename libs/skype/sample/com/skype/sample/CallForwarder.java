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
 * Contributors: Koji Hisano - initial API and implementation
 ******************************************************************************/
package com.skype.sample;

import com.skype.Call;
import com.skype.CallAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.Profile.CallForwardingRule;

public class CallForwarder {
    public static void main(String[] args) throws Exception {
        Skype.setDeamon(false);
        Skype.addCallListener(new CallAdapter() {
            @Override
            public void callReceived(Call receivedCall) throws SkypeException {
                CallForwardingRule[] oldRules = Skype.getProfile().getAllCallForwardingRules();
                Skype.getProfile().setAllCallForwardingRules(new CallForwardingRule[] { new CallForwardingRule(0, 30, "echo123") });
                receivedCall.forward();
                try {
                    Thread.sleep(10000); // to prevent finishing this call
                } catch (InterruptedException e) {
                }
                Skype.getProfile().setAllCallForwardingRules(oldRules);
            }
        });
    }
}
