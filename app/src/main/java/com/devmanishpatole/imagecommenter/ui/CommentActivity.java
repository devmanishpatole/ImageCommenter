package com.devmanishpatole.imagecommenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.devmanishpatole.imagecommenter.R;
import com.devmanishpatole.imagecommenter.base.BaseActivity;
import com.devmanishpatole.imagecommenter.imgur.data.ImageData;
import com.devmanishpatole.imagecommenter.imgur.viewmodel.CommentViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentActivity extends BaseActivity<CommentViewModel> {

    public static final String PARCEL_IMAGE = "PARCEL_IMAGE";

    private CommentViewModel viewModel;

    private ImageData imageData;

    @NotNull
    @Override
    public CommentViewModel getViewModel() {
        if(null == viewModel){
            viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        }
        return viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (null != intent) {
            imageData = intent.getParcelableExtra(PARCEL_IMAGE);
            if (null != imageData) {
                getSupportActionBar().setTitle(imageData.getTitle());
            }
        }

        initObserver();
        setupImage();
        handleEditText();
    }

    private void initObserver() {
        viewModel.getCommentData().observe(this, comment -> {
            if (null != comment && !comment.getComment().isEmpty()) {
                TextView oldComment = findViewById(R.id.oldComment);
                oldComment.append(comment.getComment());
                oldComment.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupImage() {
        if (null != imageData) {
            ImageView imageView = findViewById(R.id.imageView);
            Picasso.get().load(imageData.getLink()).error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);

            viewModel.getComment(imageData.getId());
        }
    }

    private void handleEditText() {
        EditText editText = findViewById(R.id.editComment);
        Button submit = findViewById(R.id.submitComment);

        submit.setOnClickListener(view -> {
            String comment = editText.getText().toString();

            if (comment.isEmpty()) {
                showMessage(R.string.enter_comment_error);
            } else {
                viewModel.saveComment(imageData.getId(), comment);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}