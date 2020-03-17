    package org.laba3;

    import java.sql.*;
    import java.util.ArrayList;

    public class DatabaseInteractor {
        
        private final String uri = "jdbc:postgresql://pg/studs";
        private final String user = "s267881";
        private final String password = "fwp380";
        private Connection connection;
        
        public DatabaseInteractor(){
            try {
                connection = DriverManager.getConnection(uri, user, password);
                PreparedStatement statement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS labuser (" +
                                "id SERIAL PRIMARY KEY, " +
                                "name VARCHAR NOT NULL)"
                );
                statement.execute();
                PreparedStatement statement2 = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS labpoints (" +
                        "pid SERIAL PRIMARY KEY," +
                        "x FLOAT NOT NULL," +
                        "y FLOAT NOT NULL," +
                        "r FLOAT NOT NULL," +
                        "result INTEGER NOT NULL," +
                        "userid INTEGER NOT NULL)"
                );
                statement2.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        
        public int loadOrCreateUser(String name) throws SQLException{
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM labuser WHERE name=?"
            );
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
            else
            {
                PreparedStatement statement2 = connection.prepareStatement(
                        "INSERT INTO labuser(name) " +
                        "VALUES(?) "
                );
                statement2.setString(1,name);
                statement2.executeUpdate();
                PreparedStatement statement3 = connection.prepareStatement(
                        "SELECT * FORM labuser WHERE name=?"
                );
                statement3.setString(1, name);
                ResultSet rs1 = statement3.executeQuery();
                if (rs.next())
                    return rs.getInt("id");
                return 0;
            }

        }

        public ArrayList<Point> loadUserData(String name) throws SQLException{
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM labuser u INNER JOIN labpoints l ON l.userid = u.id WHERE name = ? "
            );
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            ArrayList<Point> list = new ArrayList<>();
            while(rs.next()){
                Point p = new Point(rs.getDouble("x"),rs.getDouble("y"),rs.getDouble("r"));
                p.setId(rs.getInt("pid"));
                list.add(p);
            }
            return list;
        }

        public void deletePoint(Point p)throws SQLException{
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM labpoints WHERE pid = ?"
            );
            statement.setInt(1, p.getId());
            statement.execute();
        }

        public Point insertPoint(int userid, double x, double y, double r) throws SQLException{
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO labpoints(x, y, r, result, userid) " +
                    "VALUES(?, ?, ?, ?, ?) returning pid",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setDouble(1, x);
            statement.setDouble(2, y);
            statement.setDouble(3, r);
            Point p = new Point(x, y, r);
            statement.setInt(4, p.getResult());
            statement.setInt(5, userid);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Точка не удалена");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Точка не удалена");
                }
            }
            return p;
        }

        public void updatePoint(Point p) {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE labpoints " +
                        "SET result=?, r=? " +
                        "WHERE pid=?"
                );
                statement.setInt(1, p.getResult());
                statement.setDouble(2, p.getR());
                statement.setInt(3, p.getId());
                statement.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
