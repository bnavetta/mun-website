const SET_USER_INFO = "user-info:set";

export function userInfoReducer(state = {}, action) {
    switch (action.type) {
        case SET_USER_INFO:
            return action.payload;
        default:
            return state;
    }
}

export function setUserInfo(info) {
    return { type: SET_USER_INFO, payload: info };
}

export const selectUserInfo = state => state.userInfo;
