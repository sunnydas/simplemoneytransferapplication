package com.transfer.money.repository.dataaccess.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface DBFacade {

    public Connection getConnection() throws Exception;

    public PreparedStatement generatePreparedStatement(Connection conn,String sql) throws Exception;

    public void executeUpdate(PreparedStatement preparedStatement) throws Exception;

    public void closeSilently(Connection conn, PreparedStatement preparedStatement);

    public void closeSilently(Connection conn);

    public ResultSet executeQuery(PreparedStatement queryPrepStatement) throws Exception;

    public void createTables(Connection connection) throws Exception;

    public void rollBackSilently(Connection connection);

    public void cleanupBeforeExit(boolean error,Connection conn,PreparedStatement statement);

}
