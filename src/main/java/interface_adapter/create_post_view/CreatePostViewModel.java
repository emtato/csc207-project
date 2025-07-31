package interface_adapter.create_post_view;/**
 * Created by Emilia on 2025-07-31!
 * Description:
 * ^ • ω • ^
 */

import interface_adapter.ViewModel;


public class CreatePostViewModel extends ViewModel<CreatePostState> {


    public CreatePostViewModel() {
        super("Post");
        setState(new CreatePostState());
    }
}
