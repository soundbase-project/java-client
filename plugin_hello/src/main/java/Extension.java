import com.soundhive.plugin.Plugin;

public class Extension implements Plugin {
    @Override
    public void start() {
        System.out.println("ran frum plugin");
    }
}
