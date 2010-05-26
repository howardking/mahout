/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.cf.taste.hadoop.similarity.item;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.cf.taste.hadoop.EntityPrefWritable;
import org.apache.mahout.cf.taste.hadoop.EntityPrefWritableArrayWritable;
import org.apache.mahout.math.VarLongWritable;

/**
 * For each single item, collect all users with their preferences
 * (thereby building the item vectors of the user-item-matrix)
 */
public final class ToItemVectorReducer extends
    Reducer<VarLongWritable,EntityPrefWritable,VarLongWritable,EntityPrefWritableArrayWritable> {

  @Override
  public void reduce(VarLongWritable item,
                     Iterable<EntityPrefWritable> userPrefs,
                     Context context)
      throws IOException, InterruptedException {

    Set<EntityPrefWritable> collectedUserPrefs = new HashSet<EntityPrefWritable>();

    for (EntityPrefWritable writable : userPrefs) {
      collectedUserPrefs.add(writable.clone());
    }

    context.write(item, new EntityPrefWritableArrayWritable(
        collectedUserPrefs.toArray(new EntityPrefWritable[collectedUserPrefs.size()])));
  }

}
