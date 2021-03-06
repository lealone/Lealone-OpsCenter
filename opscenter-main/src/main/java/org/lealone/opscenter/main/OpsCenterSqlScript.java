/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.opscenter.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class OpsCenterSqlScript {

    public static void main(String[] args) throws Exception {
        new OpsCenterSqlScript().run(args);
    }

    @SuppressWarnings("unused")
    private String tableDir = "../opscenter-dal/src/main/resources";
    private String serviceDir = "../opscenter-service/src/main/resources";

    private void run(String[] args) throws Exception {
        parseArgs(args);

        // 创建OpsCenter数据库
        String jdbcUrl = "jdbc:lealone:tcp://localhost/lealone?user=root&password=";
        // runSql(jdbcUrl, "drop database if exists opscenter");
        runSql(jdbcUrl, "create database if not exists opscenter");

        // 执行建表脚本，同时自动生成对应的模型类的代码
        // runScript(tableDir + "/tables.sql");

        // 初始化数据
        // runScript(tableDir + "/init-data.sql");

        // 执行服务创建脚本，同时自动生成对应的服务接口代码
        runScript(serviceDir + "/services.sql");
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }

        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            switch (a) {
            case "-tableDir":
                tableDir = args[++i];
                break;
            case "-serviceDir":
                serviceDir = args[++i];
                break;
            default:
                System.out.println("选项名 '" + a + "' 无效");
                System.exit(-1);
            }
        }
    }

    static void runScript(String scriptFile) throws Exception {
        String jdbcUrl = "jdbc:lealone:tcp://localhost/opscenter?user=root&password=";
        runSql(jdbcUrl, "RUNSCRIPT FROM '" + scriptFile + "'");
    }

    static void runSql(String url, String sql) throws Exception {
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("execute sql: " + sql);
        }
    }
}
