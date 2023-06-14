/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors as indicated
 * by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.resource.adapter.mail.inflow;

import jakarta.mail.*;
import jakarta.mail.Flags.Flag;


/**
 * Represents a POP3 mail folder
 *
 * @author <a href="mailto:scott.stark@jboss.org">Scott Stark</a>
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public class POP3MailFolder extends MailFolder {
    private boolean flush;

    /**
     * Constructor
     *
     * @param spec The mail activation
     */
    public POP3MailFolder(MailActivationSpec spec) {
        super(spec);
        this.flush = spec.isFlush();
    }

    /**
     * Get the messages from a folder
     *
     * @param folder The folder
     * @return The messages
     * @throws jakarta.mail.MessagingException Thrown if there is an error
     */
    protected Message[] getMessages(Folder folder) throws MessagingException {
        return folder.getMessages();
    }

    /**
     * Open a store
     *
     * @param session The mail session
     * @return The store
     * @throws jakarta.mail.NoSuchProviderException Thrown if there is no provider
     */
    protected Store openStore(Session session) throws NoSuchProviderException {
        return session.getStore("pop3");
    }

    /**
     * Mark a message as seen
     *
     * @param message The messages
     * @throws jakarta.mail.MessagingException Thrown if there is an error
     */
    protected void markMessageSeen(Message message) throws MessagingException {
        message.setFlag(Flag.DELETED, true);
    }

    /**
     * Close a store
     *
     * @param success Check for successful close
     * @param store   The store
     * @param folder  The folder
     * @throws jakarta.mail.MessagingException Thrown if there is an error
     */
    protected void closeStore(boolean success, Store store, Folder folder) throws MessagingException {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(success && flush);
            }
        } finally {
            if (store != null && store.isConnected()) {
                store.close();
            }
        }
    }
}
