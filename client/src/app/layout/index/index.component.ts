import {Component, OnInit} from '@angular/core';
import {Post} from "../../models/Post";
import {PostService} from "../../service/post.service";
import {User} from "../../models/User";
import {UserService} from "../../service/user.service";
import {CommentService} from "../../service/comment.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  isPostsLoaded = false;
  isUserDataLoaded = false;
  posts!: Post[];
  user!: User;

  constructor(private postService: PostService,
              private userService: UserService,
              private commentService: CommentService,
              private notificationService: NotificationService,
              private imageService: ImageUploadService,
              ) {
  }

  ngOnInit(): void {
    this.postService.getAllPosts()
      .subscribe(data => {
        console.log(data);
        this.posts = data;
        this.getImagesToPosts(this.posts);
        this.getCommentsToPosts(this.posts);
        this.isPostsLoaded = true;
      });

    this.userService.getCurrentUser()
      .subscribe(data => {
        this.user = data;
        this.isUserDataLoaded = true;
      })
  }

  getImagesToPosts(posts: Post[]): void {
    posts.forEach(p => {
      this.imageService.getImageToPost(p.id)
        .subscribe(data => {
          p.image = data.imageBytes;
        })
    });
  }

  getCommentsToPosts(posts: Post[]): void {
    posts.forEach(p => {
      this.commentService.getCommentsToPost(p.id)
        .subscribe(data => {
          p.comments = data;
        })
    });
  }

  likePost(postId: number | undefined, postIndex: number): void {
    const post = this.posts[postIndex];
    console.log(post);

    if (!post.userLiked?.includes(this.user.username)) {
      this.postService.likePost(postId, this.user.username)
        .subscribe(() => {
          post.userLiked?.push(this.user.username);
          this.notificationService.showSneakBar('Liked!');
        });
    } else {
      this.postService.likePost(postId, this.user.username)
        .subscribe(() => {
          const index = post.userLiked?.indexOf(this.user.username, 0);
          if (index !== undefined && index > -1) {
            post.userLiked?.splice(index, 1);
          }
        })
    }
  }

  postComment(message: string, postId: number | undefined, postIndex: number): void {
    const post = this.posts[postIndex];
    console.log(post);
    this.commentService.addCommentToPost(postId, message)
      .subscribe(data => {
        console.log(data);
        post.comments?.push(data);
      });
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64, ' + img;
  }

  getInputValue(event: any): string {
    const inputElement = event.target as HTMLInputElement; // Cast the event target
    return inputElement.value;
  }
}
