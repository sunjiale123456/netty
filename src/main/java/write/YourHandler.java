package write;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class YourHandler extends ChannelInboundHandlerAdapter {
    private Connection connection;
    private PreparedStatement statement;

    public YourHandler() {
        // 建立与ClickHouse的连接
        try {
            connection = DriverManager.getConnection("jdbc:clickhouse://localhost:8123/default");
            statement = connection.prepareStatement("INSERT INTO your_table (column1, column2) VALUES (?, ?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收到数据
        ByteBuf byteBuf = (ByteBuf) msg;
        String message = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("Received message: " + message);

        // 将数据写入ClickHouse
        try {
            statement.setString(1, message); // 设置第一个参数
            statement.setInt(2, 123); // 设置第二个参数
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            byteBuf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 发生异常时的处理逻辑
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 关闭与ClickHouse的连接
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
