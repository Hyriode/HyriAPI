package fr.hyriode.api.server.join;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/04/2022 at 21:29
 */
public enum HyriJoinResponse {

    ALLOW,
    DENY_OTHER,
    DENY_FULL,
    DENY_SLOTS,
    DENY_STATE;

    public boolean isAllowed() {
        return this == ALLOW;
    }

}
