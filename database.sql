CREATE DATABASE IF NOT EXISTS test;

use test;

CREATE TABLE IF NOT EXISTS commit(
  commitID VARCHAR(50) PRIMARY KEY,
  buildDate VARCHAR(30) NOT NULL,
  buildResult BIT(1) NOT NULL,
  buildLogs MEDIUMTEXT NOT NULL
)
