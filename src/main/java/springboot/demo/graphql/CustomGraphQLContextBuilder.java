package springboot.demo.graphql;

import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.DefaultGraphQLContextBuilder;
import graphql.kickstart.execution.context.GraphQLKickstartContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContextBuilder;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springboot.demo.graphql.dataloader.AuthorDataLoader;

@Component
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {

  @Autowired
  private AuthorDataLoader authorDataLoader;

  @Override
  public GraphQLKickstartContext build() {
    return new DefaultGraphQLContext();
  }

  @Override
  public GraphQLKickstartContext build(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {
    var context = new DefaultGraphQLContext(buildDataLoaderRegistry());
    context.put(HttpServletRequest.class, httpServletRequest);
    context.put(HttpServletResponse.class, httpServletResponse);
    return context;
  }

  @Override
  public GraphQLKickstartContext build(Session session, HandshakeRequest handshakeRequest) {
    var context = new DefaultGraphQLContext(buildDataLoaderRegistry());
    context.put(Session.class, session);
    context.put(HandshakeRequest.class, handshakeRequest);
    return context;
  }

  public DataLoaderRegistry buildDataLoaderRegistry() {
    DataLoaderRegistry registry = new DataLoaderRegistry();
    registry.register(AuthorDataLoader.KEY, authorDataLoader.getLoader());
    return registry;
  }
}
