package cn.com.connext.msf.webapp.config;

import cn.com.connext.msf.framework.query.QueryInfo;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.spi.schema.contexts.ModelContext.inputParam;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SwaggerQueryInfoEncoder implements OperationBuilderPlugin {
    private final TypeNameExtractor nameExtractor;
    private final TypeResolver resolver;
    private final ResolvedType queryInfoType;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public SwaggerQueryInfoEncoder(TypeNameExtractor nameExtractor, TypeResolver resolver) {
        this.nameExtractor = nameExtractor;
        this.resolver = resolver;
        this.queryInfoType = resolver.resolve(QueryInfo.class);
    }

    @Override
    public void apply(OperationContext context) {
        List<ResolvedMethodParameter> methodParameters = context.getParameters();
        List<Parameter> parameters = newArrayList();

        for (ResolvedMethodParameter methodParameter : methodParameters) {
            ResolvedType resolvedType = methodParameter.getParameterType();

            if (queryInfoType.equals(resolvedType)) {
                ParameterContext parameterContext = new ParameterContext(methodParameter,
                        new ParameterBuilder(),
                        context.getDocumentationContext(),
                        context.getGenericsNamingStrategy(),
                        context);
                Function<ResolvedType, ? extends ModelReference> factory = createModelRefFactory(parameterContext);

                ModelReference intModel = factory.apply(resolver.resolve(Integer.TYPE));
                ModelReference stringModel = factory.apply(resolver.resolve(String.class));
                ModelReference stringListModel = factory.apply(resolver.resolve(List.class, String.class));

                parameters.add(new ParameterBuilder()
                        .parameterType("query")
                        .name("expression")
                        .modelRef(stringModel)
                        .description("查询表达式").build());

                if (Objects.equals("org.springframework.data.domain.Page", context.getReturnType().getErasedType().getName())) {
                    parameters.add(new ParameterBuilder()
                            .parameterType("query")
                            .name("page")
                            .modelRef(intModel)
                            .description("起始页码").build());

                    parameters.add(new ParameterBuilder()
                            .parameterType("query")
                            .name("size")
                            .modelRef(intModel)
                            .description("分页大小").build());
                }

                if (Objects.equals("cn.com.connext.msf.framework.query.Scroll", context.getReturnType().getErasedType().getName())) {
                    parameters.add(new ParameterBuilder()
                            .parameterType("query")
                            .name("scroll_size")
                            .modelRef(intModel)
                            .description("批次大小").build());
                }

                if (context.getReturnType().getTypeName().contains("<com.fasterxml.jackson.databind.node.ObjectNode>")) {
                    parameters.add(new ParameterBuilder()
                            .parameterType("query")
                            .name("fields")
                            .modelRef(stringModel)
                            .description("字段名称").build());
                }

                parameters.add(new ParameterBuilder()
                        .parameterType("query")
                        .name("sort")
                        .modelRef(stringListModel)
                        .allowMultiple(true)
                        .description("排序方式").build());

                context.operationBuilder().parameters(parameters);
            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private Function<ResolvedType, ? extends ModelReference> createModelRefFactory(ParameterContext context) {
        ModelContext modelContext = inputParam(
                context.getGroupName(),
                context.resolvedMethodParameter().getParameterType(),
                context.getDocumentationType(),
                context.getAlternateTypeProvider(),
                context.getGenericNamingStrategy(),
                context.getIgnorableParameterTypes());
        return ResolvedTypes.modelRefFactory(modelContext, nameExtractor);
    }
}