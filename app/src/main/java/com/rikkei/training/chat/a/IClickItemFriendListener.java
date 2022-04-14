package com.rikkei.training.chat.a;

import com.rikkei.training.chat.modle.Conversation;
import com.rikkei.training.chat.modle.User;

public interface IClickItemFriendListener {

    void onClickItemFriend(User user);
    void onClickItemFriend(Conversation conversation);
}
