package cn.com.connext.msf.framework.auth;

import java.util.*;

public class AuthorityGroup {
    private String code;
    private String name;
    private List<AuthorityOption> options;

    public static AuthorityGroup from(String code, String name, AuthorityOption... authorityOptions) {
        AuthorityGroup authorityGroup = new AuthorityGroup();
        authorityGroup.code = code;
        authorityGroup.name = name;
        authorityGroup.options = Arrays.asList(authorityOptions);
        return authorityGroup;
    }

    @SafeVarargs
    public static List<AuthorityGroup> mergeAuthorityGroup(List<AuthorityGroup> group, List<AuthorityGroup>... otherGroups) {
        List<AuthorityGroup> mergedAuthorityGroups = new ArrayList<>(group);

        for (List<AuthorityGroup> otherGroupList : otherGroups) {
            mergedAuthorityGroups = mergeAuthorityGroup(mergedAuthorityGroups, otherGroupList);
        }

        return mergedAuthorityGroups;
    }

    public static List<String> getAuthorityList(List<AuthorityGroup> authorityGroupList) {
        Set<String> authorities = new HashSet<>();
        authorityGroupList.forEach(authorityGroup ->
                authorityGroup.getOptions().forEach(authorityOption ->
                        authorities.add(authorityOption.getCode())));
        return new ArrayList<>(authorities);
    }

    private static List<AuthorityGroup> mergeAuthorityGroup(List<AuthorityGroup> groups, List<AuthorityGroup> otherGroups) {
        List<AuthorityGroup> mergedAuthorityGroups = new ArrayList<>(groups);

        otherGroups.forEach(otherGroup -> {
            AuthorityGroup elementInGroupList = elementInGroupList(otherGroup, mergedAuthorityGroups);
            if (elementInGroupList == null) {
                mergedAuthorityGroups.add(otherGroup);
            } else {
                Set<AuthorityOption> options = new HashSet<>(elementInGroupList.getOptions());
                options.addAll(otherGroup.getOptions());
                elementInGroupList.setOptions(new ArrayList<>(options));
            }
        });

        return mergedAuthorityGroups;
    }

    private static AuthorityGroup elementInGroupList(AuthorityGroup authorityGroup, List<AuthorityGroup> groupList) {
        for (AuthorityGroup group : groupList) {
            if (authorityGroup.getCode().equals(group.getCode())) {
                return group;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public List<AuthorityOption> getOptions() {
        return options;
    }

    public void setOptions(List<AuthorityOption> options) {
        this.options = options;
    }

}
