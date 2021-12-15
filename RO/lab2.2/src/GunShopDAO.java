import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GunShopDAO {
    private final Connection connection;

    public GunShopDAO() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        final String user = "postgres";
        final String password = "14062002";
        final String url = "jdbc:postgresql://localhost:5432/postgres@localhost";

        this.connection = DriverManager.getConnection(url, user, password);
    }

    public GunShopDAO(String DBName, String ip, int port) throws Exception {
        Class.forName("org.postgresql.Driver");

        final String user = "guest";
        final String password = "123123";
        final String url = String.format("jdbc:postgresql://%s:%d/%s", ip, port, DBName);

        connection = DriverManager.getConnection(url, user, password);
    }

    public List<KnifeType> getKnifeTypes() {
        PreparedStatement statement = null;
        List<KnifeType> knifeTypes = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT id, name FROM KnifeTypes");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                knifeTypes.add(new KnifeType(result.getInt("id"), result.getString("name")));
            }
            result.close();
        } catch (SQLException e) {
            System.out.println("Error during getting knifeTypes");
            System.out.println(" >> " + e.getMessage());
        } finally {
            return knifeTypes;
        }
    }

    public void showKnifeTypes() {
        System.out.println("KnifeTypes:");

        for (KnifeType knifeType : getKnifeTypes()) {
            System.out.println(knifeType);
        }
    }

    public boolean addKnifeType(KnifeType knifeType) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO  KnifeTypes (name) VALUES (?)");
            statement.setString(1, knifeType.getName());

            statement.executeUpdate();
            System.out.printf("KnifeType %s successfully added!%n", knifeType.toString());
            return true;
        } catch (SQLException e) {
            System.out.printf("KnifeType %s was not added!%n", knifeType.toString());
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public boolean updateKnifeType(String oldKnifeType, KnifeType newKnifeType) throws Exception {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("Update KnifeTypes Set name=? Where name=?");

            statement.setString(1, newKnifeType.getName());
            statement.setString(2, oldKnifeType);

            statement.executeUpdate();
            System.out.printf("KnifeType %s was successfully updated!%n", oldKnifeType);
            return true;
        } catch (SQLException e) {
            System.out.printf("KnifeType %s was not updated!%n", oldKnifeType);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public boolean deleteKnifeType(String name) {
        PreparedStatement deleteKnifeTypeStatement = null, deleteKnivesStatement = null;
        try {
            deleteKnifeTypeStatement = connection.prepareStatement("DELETE FROM KnifeTypes WHERE KnifeTypes.name = ?");
            deleteKnifeTypeStatement.setString(1, name);

            deleteKnivesStatement = connection.prepareStatement("DELETE FROM Knives WHERE Knives.knifeTypeId = " +
                    "(SELECT KnifeTypes.id FROM KnifeTypes WHERE KnifeTypes.name = ?)");
            deleteKnivesStatement.setString(1, name);

            deleteKnivesStatement.executeUpdate();
            int result = deleteKnifeTypeStatement.executeUpdate();

            if (result > 0) {
                System.out.printf("KnifeType %s was successfully deleted%n", name);
                return true;
            } else {
                System.out.printf("There is no knifeType with name %s%n", name);
                return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error during deleting knifeType with name %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public List<Knife> getKnives() {
        PreparedStatement statement = null;
        List<Knife> knives = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT KnifeTypes.id AS knifeTypeId, KnifeTypes.name AS knifeTypeName,  Knives.id" +
                    " AS knifeId,  Knives.name AS knifeName, Knives.length AS knifeLength\n" +
                    "FROM KnifeTypes INNER JOIN Knives on KnifeTypes.id = Knives.knifeTypeid");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                knives.add(new Knife(result.getInt("knifeId"),
                                result.getString("knifeName"),
                                result.getFloat("knifeLength"),
                                new KnifeType(result.getInt("knifeTypeId"),
                                        result.getString("knifeTypeName")
                                )
                        )
                );
            }
            result.close();

        } catch (SQLException e) {
            System.out.println("Error during getting knives");
            System.out.println(" >> " + e.getMessage());
        } finally {
            return knives;
        }
    }

    public void showKnives() {
        System.out.println("Knives:");

        for (Knife knife : getKnives()) {
            System.out.println(knife);
        }
    }


    public boolean addKnife(Knife knife) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO knives (name, length, knifeTypeId) VALUES (?, ?, ?)");
            statement.setString(1, knife.getName());
            statement.setFloat(2, knife.getLength());
            statement.setInt(3, getKnifeTypeId(knife.getKnifeType().getName()));

            statement.executeUpdate();
            System.out.printf("Knife %s successfully added!%n", knife.toString());
            return true;
        } catch (SQLException e) {
            System.out.printf("Knife %s was not added!%n", knife.toString());
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public boolean updateKnife(Knife oldKnife, Knife newKnife) throws Exception {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("Update Knives Set name=?, length=?, knifeTypeId=? Where name=?");

            statement.setString(1, newKnife.getName());
            statement.setFloat(2, newKnife.getLength());
            statement.setInt(3, newKnife.getKnifeType().getId());
            statement.setString(4, oldKnife.getName());

            statement.executeUpdate();
            System.out.printf("Knife %s was successfully updated!%n", oldKnife.toString());
            return true;
        } catch (SQLException e) {
            System.out.printf("Knife %s was not updated!%n", oldKnife.toString());
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public boolean deleteKnife(String name) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM Knives WHERE Knives.name = ?");

            statement.setString(1, name);

            int result = statement.executeUpdate();

            if (result > 0) {
                System.out.printf("Knife %s was successfully deleted%n", name);
                return true;
            } else {
                System.out.printf("There is no knife with name %s%n", name);
                return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error during deleting knife with name %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }


    public int getNumberOfKnivesByKnifeType(String knifeTypeName) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT COUNT(*) AS result \n" +
                    "FROM Knives INNER JOIN KnifeTypes ON Knives.knifeTypeId = KnifeTypes.id\n" +
                    "WHERE KnifeTypes.name = ?");

            statement.setString(1, knifeTypeName);

            ResultSet queryResult = statement.executeQuery();
            queryResult.next();
            int result = queryResult.getInt("result");

            queryResult.close();

            return result;
        } catch (SQLException e) {
            System.out.printf("Error during getting knives with knifeType %s%n", knifeTypeName);
            System.out.println(" >> " + e.getMessage());
            return -1;
        }
    }


    public Knife getKnifeByName(String knifeName) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT Knives.id AS knifeId, Knives.knifeTypeId AS knifeTypeId, Knives.name " +
                    "AS knifeName, Knives.length AS length FROM Knives WHERE Knives.name=?");
            statement.setString(1, knifeName);

            ResultSet queryResult = statement.executeQuery();
            queryResult.next();
            Knife knife = new Knife(queryResult.getInt("knifeId"), queryResult.getString("knifeName"),
                    queryResult.getFloat("length"), getKnifeTypeById(queryResult.getInt("knifeTypeId")));

            queryResult.close();

            return knife;


        } catch (SQLException e) {
            System.out.printf("Error during getting knife by name %s%n", knifeName);
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }


    public List<Knife> getKnivesByKnifeType(String knifeTypeName) {
        PreparedStatement statement = null;
        List<Knife> knives = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT KnifeTypes.id AS knifeTypeId, KnifeTypes.name AS knifeTypeName,  Knives.id" +
                    " AS knifeId,  Knives.name AS knifeName, Knives.length AS knifeLength\n" +
                    "FROM KnifeTypes INNER JOIN Knives on KnifeTypes.id = Knives.knifeTypeid " +
                    "WHERE KnifeTypes.name = ?");

            statement.setString(1, knifeTypeName);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                knives.add(new Knife(result.getInt("knifeId"),
                                result.getString("knifeName"),
                                result.getFloat("knifeLength"),
                                new KnifeType(result.getInt("knifeTypeId"),
                                        result.getString("knifeTypeName")
                                )
                        )
                );
            }
            result.close();
            return knives;
        } catch (SQLException e) {
            System.out.printf("Error during getting knives by knifeType %s%n", knifeTypeName);
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }


    public int getKnifeTypeId(String name) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement("SELECT id FROM KnifeTypes WHERE KnifeTypes.name = ?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            result.next();
            int id = result.getInt("id");
            result.close();
            return id;
        } catch (SQLException e) {
            System.out.printf("Error during getting knifeType id for %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return -1;
        }
    }


    public KnifeType getKnifeTypeById(int id) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement("SELECT name FROM KnifeTypes WHERE KnifeTypes.id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            String knifeTypeName = result.getString("name");
            result.close();
            return new KnifeType(id, knifeTypeName);
        } catch (SQLException e) {
            System.out.println("Error during getting knifeType");
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }


    public void stop() throws SQLException {
        connection.close();
    }
}
