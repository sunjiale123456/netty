package write;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;
import java.sql.PreparedStatement;


public class ClickHouseHandler extends ChannelInboundHandlerAdapter {

    private ClickHouseConnection connection;
    private PreparedStatement statement;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 建立 ClickHouse 连接
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setUser("default");
        properties.setPassword("Gds!23d3");
        ClickHouseDataSource dataSource = new ClickHouseDataSource("jdbc:clickhouse://10.91.125.4:8123", properties);
        connection = dataSource.getConnection();
        // 准备 ClickHouse 插入语句
        String insertQuery = "INSERT INTO your_table (column1, column2) VALUES (?, ?)";
        statement = connection.prepareStatement(insertQuery);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理接收到的数据
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String receivedData = new String(bytes);

        // 将数据写入 ClickHouse
        String[] dataParts = receivedData.split(",");
        if (dataParts.length == 2) {
            String column1 = dataParts[0];
            String column2 = dataParts[1];

            statement.setString(1, column1);
            statement.setString(2, column2);
            statement.addBatch();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 执行 ClickHouse 插入操作并关闭连接
        statement.executeBatch();
        statement.close();
        connection.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}