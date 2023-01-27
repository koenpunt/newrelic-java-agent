/*
 *
 *  * Copyright 2023 New Relic Corporation. All rights reserved.
 *  * SPDX-License-Identifier: Apache-2.0
 *
 */

package org.apache.kafka.connect.runtime;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.kafka.connect.KafkaConnectMetricsReporter;
import org.apache.kafka.connect.util.ConnectorTaskId;

import java.util.concurrent.atomic.AtomicBoolean;

@Weave(originalName = "org.apache.kafka.connect.runtime.WorkerTask", type = MatchType.BaseClass)
class WorkerTask_Instrumentation {

    WorkerTask.TaskMetricsGroup taskMetricsGroup() {
        return Weaver.callOriginal();
    }

    public void initialize(TaskConfig taskConfig) {

        WorkerTask.TaskMetricsGroup taskMetricsGroup = taskMetricsGroup();
        if (taskMetricsGroup != null) {
            KafkaConnectMetricsReporter.initialize(taskMetricsGroup.metricGroup());
        }
        Weaver.callOriginal();
    }
}
