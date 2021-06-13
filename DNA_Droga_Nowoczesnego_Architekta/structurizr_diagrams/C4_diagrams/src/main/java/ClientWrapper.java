import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.api.StructurizrClientException;

public class ClientWrapper {

    private final StructurizrClient structurizrClient;
    private final long workspaceId;

    public ClientWrapper(String api, String secret, long workspaceId) {
        this.structurizrClient = new StructurizrClient(api, secret);
        this.workspaceId = workspaceId;
    }

    public void putWorkspace(Workspace w) throws StructurizrClientException {
        structurizrClient.putWorkspace(workspaceId, w);
    }

}
