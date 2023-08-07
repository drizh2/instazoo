import {Component, OnInit} from '@angular/core';
import {Post} from "../../models/Post";
import {PostService} from "../../service/post.service";
import {ImageUploadService} from "../../service/image-upload.service";
import {CommentService} from "../../service/comment.service";
import {NotificationService} from "../../service/notification.service";

@Component({
  selector: 'app-user-posts',
  templateUrl: './user-posts.component.html',
  styleUrls: ['./user-posts.component.css']
})
export class UserPostsComponent implements OnInit {

  isUserPostsLoaded = false;
  posts!: Post[];

  constructor(private postService: PostService,
              private imageService: ImageUploadService,
              private commentService: CommentService,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.postService.getPostsForCurrentUser()
      .subscribe(data => {
        console.log(data);
        this.posts = data;
        this.getImagesToPosts(this.posts);
        this.getCommentsToPosts(this.posts);
        this.isUserPostsLoaded = true;
      })
  }

  private getImagesToPosts(posts: Post[]) {
    posts.forEach(p => {
      this.imageService.getImageToPost(p.id)
        .subscribe(data => {
          p.image = data.imageBytes;
        })
    });
  }

  private getCommentsToPosts(posts: Post[]) {
    posts.forEach(p => {
      this.commentService.getCommentsToPost(p.id)
        .subscribe(data => {
          p.comments = data;
        })
    });
  }

  removePost(post: Post, index: number): void {
    console.log(post);
    const result = confirm('Do you really want to delete this post?');
    if (result) {
      this.postService.delete(post.id)
        .subscribe(() => {
          this.posts.splice(index, 1);
          this.notificationService.showSneakBar('The post has been deleted successfully!')
        });
    }
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64, ' + img;
  }

  deleteComment(commentId: number | undefined, postIndex: number, commentIndex: number): void {
    const post = this.posts[postIndex];
    const result = confirm('Do you really want to delete this post?');
    if (result) {
      this.commentService.deleteComment(commentId)
        .subscribe(data => {
          post.comments?.splice(commentIndex, 1);
          this.notificationService.showSneakBar('The comment has been successfully deleted!');
        });
    }
  }
}
