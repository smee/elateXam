/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.thorstenberger.taskmodel.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * JAXB Utilites to pool JAXBContext and related objects. 
 * Extracted from org.apache.axis2.jaxws.message.databinding of project JAX-WS
 */
public class JAXBUtils {

    private static final Log log = LogFactory.getLog(JAXBUtils.class);

    private static Pool<JAXBContext, Marshaller>       mpool = new Pool<JAXBContext, Marshaller>();
    private static Pool<JAXBContext, Unmarshaller>     upool = new Pool<JAXBContext, Unmarshaller>();
    
    // From Lizet Ernand:
    // If you really care about the performance, 
    // and/or your application is going to read a lot of small documents, 
    // then creating Unmarshaller could be relatively an expensive operation. 
    // In that case, consider pooling Unmarshaller objects.
    // Different threads may reuse one Unmarshaller instance, 
    // as long as you don't use one instance from two threads at the same time. 
    // ENABLE_ADV_POOLING is false...which means they are obtained from the JAXBContext instead of
    // from the pool.
    private static boolean ENABLE_MARSHALL_POOLING = true;
    private static boolean ENABLE_UNMARSHALL_POOLING = true;
    
    private static int MAX_LOAD_FACTOR = 32;  // Maximum number of JAXBContext to store

    // Construction Type
    public enum CONSTRUCTION_TYPE {
        BY_CLASS_ARRAY, BY_CONTEXT_PATH, UNKNOWN}

    ;


   


    /**
     * Get the unmarshaller.  You must call releaseUnmarshaller to put it back into the pool
     *
     * @param context JAXBContext
     * @return Unmarshaller
     * @throws JAXBException
     */
    public static Unmarshaller getJAXBUnmarshaller(JAXBContext context) throws JAXBException {
        if (!ENABLE_UNMARSHALL_POOLING) {
            if (log.isDebugEnabled()) {
                log.debug("Unmarshaller created [no pooling]");
            }
            return context.createUnmarshaller();
        }
        Unmarshaller unm = upool.get(context);
        if (unm == null) {
            if (log.isDebugEnabled()) {
                log.debug("Unmarshaller created [not in pool]");
            }
            unm = context.createUnmarshaller();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Unmarshaller obtained [from  pool]");
            }
        }
        return unm;
    }

    /**
     * Release Unmarshaller Do not call this method if an exception occurred while using the
     * Unmarshaller. We object my be in an invalid state.
     *
     * @param context      JAXBContext
     * @param unmarshaller Unmarshaller
     */
    public static void releaseJAXBUnmarshaller(JAXBContext context, Unmarshaller unmarshaller) {
        if (log.isDebugEnabled()) {
            log.debug("Unmarshaller placed back into pool");
        }
        if (ENABLE_UNMARSHALL_POOLING) {
            upool.put(context, unmarshaller);
        }
    }

    /**
     * Get JAXBMarshaller
     *
     * @param context JAXBContext
     * @return Marshaller
     * @throws JAXBException
     */
    public static Marshaller getJAXBMarshaller(JAXBContext context) throws JAXBException {
        Marshaller m = null;
        if (!ENABLE_MARSHALL_POOLING) {
            if (log.isDebugEnabled()) {
                log.debug("Marshaller created [no pooling]");
            }
            m = context.createMarshaller();
        } else {
            m = mpool.get(context);
            if (m == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Marshaller created [not in pool]");
                }
                m = context.createMarshaller();
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Marshaller obtained [from  pool]");
                }
            }
        }
        return m;
    }

    /**
     * releaseJAXBMarshalller Do not call this method if an exception occurred while using the
     * Marshaller. We object my be in an invalid state.
     *
     * @param context    JAXBContext
     * @param marshaller Marshaller
     */
    public static void releaseJAXBMarshaller(JAXBContext context, Marshaller marshaller) {
        if (log.isDebugEnabled()) {
            log.debug("Marshaller placed back into pool");
        }
        if (ENABLE_MARSHALL_POOLING) {
            marshaller.setAttachmentMarshaller(null);
            mpool.put(context, marshaller);
        }
    }



    /**
     * Pool a list of items for a specific key
     *
     * @param <K> Key
     * @param <V> Pooled object
     */
    private static class Pool<K,V> {
        private Map<K,List<V>> map = new ConcurrentHashMap<K, List<V>>();

        // The maps are freed up when a LOAD FACTOR is hit
        private static int MAX_LIST_FACTOR = 10;
        
        /**
         * @param key
         * @return removed item from pool or null.
         */
        public V get(K key) {
            List<V> values = getValues(key);
            synchronized (values) {
                if(values.size()>0) {
                    return values.remove(values.size()-1);
                }
            }
            return null;
        }

        /**
         * Add item back to pool
         * @param key
         * @param value
         */
        public void put(K key, V value) {
            adjustSize();
            List<V> values = getValues(key);
            synchronized (values) {
                if (values.size() < MAX_LIST_FACTOR) {
                    values.add(value);
                }
            }
        }

        /**
         * Get or create a list of the values for the key
         * @param key
         * @return list of values.
         */
        private List<V> getValues(K key) {
            List<V> values = map.get(key);
            if(values !=null) {
                return values;
            }
            synchronized (this) {
                values = map.get(key);
                if(values==null) {
                    values = new ArrayList<V>();
                    map.put(key, values);
                }
                return values;
            }
        }
        
        /**
         * AdjustSize
         * When the number of keys exceeds the maximum load, half
         * of the entries are deleted.
         * 
         * The assumption is that the JAXBContexts, UnMarshallers, Marshallers, etc. require
         * a large footprint.
         */
        private void adjustSize() {
            if (map.size() > MAX_LOAD_FACTOR) {
                // Remove every other Entry in the map.
                Iterator it = map.entrySet().iterator();
                boolean removeIt = false;
                while (it.hasNext()) {
                    it.next();
                    if (removeIt) {
                        it.remove();
                    }
                    removeIt = !removeIt;
                }
            }
        }
    }

}
