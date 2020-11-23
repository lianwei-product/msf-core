package cn.com.connext.msf.server.management.boot;

import cn.com.connext.msf.framework.route.service.ServiceRoute;
import cn.com.connext.msf.framework.utils.JSON;
import cn.com.connext.msf.framework.utils.ResourceFile;
import cn.com.connext.msf.server.management.domain.RouteManager;
import cn.com.connext.msf.server.management.entity.Route;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DataInitRunner.class);
    private final RouteManager routeManager;

    public DataInitRunner(RouteManager routeManager) {
        this.routeManager = routeManager;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!routeManager.isNeedInit()) {
            List<Route> routes = routeManager.getRegisteredRoute();
            for (Route route : routes) {
                logger.info("registered route: {}", JSON.toJsonString(route));
            }
            return;
        }

        List<ServiceRoute> serviceRoutes = loadServiceRoute();
        logger.info("Start register default service route.{}", JSON.toJsonString(serviceRoutes));

        if (serviceRoutes.size() == 0) {
            return;
        }

        for (ServiceRoute serviceRoute : serviceRoutes) {
            logger.info("Register service route: {}", serviceRoute.getServiceName());
            routeManager.registerFromServiceRoute(serviceRoute, null, null);
        }
    }

    private List<ServiceRoute> loadServiceRoute() {
        List<ServiceRoute> routes = Lists.newArrayList();
        List<String> fileNames = ResourceFile.loadFileNames("route");

        for (String file : fileNames) {
            try {
                String json = ResourceFile.loadFile(file);
                ServiceRoute serviceRoute = JSON.parseObject(json, ServiceRoute.class);
                routes.add(serviceRoute);
            } catch (Exception e) {
                logger.error("Init default route error: " + e.getMessage());
            }
        }

        return routes;
    }


}
