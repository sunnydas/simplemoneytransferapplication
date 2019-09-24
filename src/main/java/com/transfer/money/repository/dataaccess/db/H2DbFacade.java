package com.transfer.money.repository.dataaccess.db;

import com.transfer.money.config.ConfigManager;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.transfer.money.repository.dataaccess.impl.MoneyTFDAOUtil.*;

public class H2DbFacade implements DBFacade{

    private static Logger logger = LoggerFactory.getLogger(H2DbFacade.class);


    private static final String extraJdbcParams = ";DB_CLOSE_DELAY=-1";


    public Connection getConnection() throws SQLException {
        JdbcDataSource ds = getJdbcDataSource();
        Connection conn = null;
        if(ds != null){
            conn = ds.getConnection();
        }
        return conn;
    }


    public PreparedStatement generatePreparedStatement(Connection conn,String sql) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(conn != null){
            preparedStatement = conn.prepareStatement(sql);
        }
        return preparedStatement;
    }

    public void executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.execute();
    }


    private JdbcDataSource getJdbcDataSource() {
        JdbcDataSource ds = null;
        String jdbUrl = ConfigManager.getProperty(JDBC_URL_KEY);
        String dbUser = ConfigManager.getProperty(DB_USER_KEY);
        String dbPassword = ConfigManager.getProperty(DB_PASSWORD_KEY);
        if(jdbUrl != null && dbUser != null && dbPassword != null) {
            ds = new JdbcDataSource();
            jdbUrl += extraJdbcParams;
            ds.setURL(jdbUrl);
            ds.setUser(dbUser);
            ds.setPassword(dbPassword);
        }
        return ds;
    }

    public void closeSilently(Connection conn, PreparedStatement preparedStatement) {
        if(preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closeSilently(conn);
    }

    public void closeSilently(Connection conn) {
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void rollBackSilently(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error("unable to rollback",e);
        }
    }

    public ResultSet executeQuery(PreparedStatement queryPrepStatement) throws SQLException {
        ResultSet resultSet = queryPrepStatement.executeQuery();
        return resultSet;
    }

    public void createTables(Connection connection) throws FileNotFoundException, SQLException {
        String sqlFileName = ConfigManager.getProperty(CREATE_TABLE_SQL_LOC_KEY);
        if(sqlFileName != null) {
            RunScript.execute(connection, new FileReader(getSqlFileHandle(sqlFileName)));
        }
    }

    public void cleanupBeforeExit(boolean error,Connection conn,PreparedStatement statement){
        if (error) {
            rollBackSilently(conn);
        }
        closeSilently(conn, statement);
    }

    private File getSqlFileHandle(String sqlFileName) {
        return new File(
                ConfigManager.class.
                        getClassLoader().
                        getResource(sqlFileName).getFile()
        );
    }
}
