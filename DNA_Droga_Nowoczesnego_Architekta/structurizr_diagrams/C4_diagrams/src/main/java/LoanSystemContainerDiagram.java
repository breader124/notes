import com.structurizr.Workspace;

public class LoanSystemContainerDiagram {
    public static void main(String[] args) {
        String api = args[0];
        String secret = args[1];
        long workspaceId = Long.parseLong(args[2]);

        ClientWrapper strClient = new ClientWrapper(api, secret, workspaceId);

        Workspace workspace = new Workspace(Constants.WORKSPACE_NAME, Constants.WORKSPACE_DESCR);
    }
}
