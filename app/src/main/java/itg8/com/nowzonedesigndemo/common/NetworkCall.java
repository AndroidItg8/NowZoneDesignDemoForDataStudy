package itg8.com.nowzonedesigndemo.common;

public class NetworkCall {

    private static RetroController controller;

    private NetworkCall(NetworkBuilder networkBuilder) {
        controller = Retro.getInstance().getController(networkBuilder.token);
    }

    public static RetroController getController() {
        return controller;
    }

    public static final class NetworkBuilder {
        String token;

        public NetworkBuilder setHeader() {
//            token = Prefs.getString(CommonMethod.TOKEN, "-1") + " " + AppApplication.getInstance().getAppToken();
//            Log.d(TAG, "setHeader: " + token);
            return this;
        }

        public NetworkCall build() {
            return new NetworkCall(this);
        }
    }
}
