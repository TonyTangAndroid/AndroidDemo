package com.tonytang.demo.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.tonytang.demo.BuildConfig;
import com.tonytang.demo.R;
import com.tonytang.demo.constants.Constants;
import com.tonytang.demo.entity.Movie;
import com.tonytang.demo.model.MoviesWrapper;
import com.tonytang.demo.ui.activity.AndroidApplication;
import com.tonytang.demo.ui.adapter.MovieAdapter;
import com.tonytang.demo.ui.decoration.DividerItemDecoration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MovieSearchFragment extends Fragment {

	private static final String TAG = "DebounceSearch";
	//this will be responsible of managing the callback from network request.
	protected final CompositeDisposable networkRequestSubscription = new CompositeDisposable();

	@Bind(R.id.edit_text)
	EditText inputSearchText;
	@Bind(R.id.recycler_view)
	RecyclerView recyclerView;
	@Bind(R.id.top_empty_view)
	FrameLayout topEmptyView;//It will be shown when the data is loading or the request has error. It will be hiddden if the result is good.
	@Bind(R.id.progress_bar)
	ProgressBar progressBar;
	@Bind(R.id.tv_empty_hint)
	TextView tvEmptyViewHint;


	private Disposable disposable;
	private MovieAdapter movieAdapter;

	public static MovieSearchFragment newInstance() {

		return new MovieSearchFragment();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_movie, container, false);
		ButterKnife.bind(this, layout);
		return layout;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setHasFixedSize(true);
		recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		movieAdapter = new MovieAdapter(getActivity());
		recyclerView.setAdapter(movieAdapter);


	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final Observable<TextViewTextChangeEvent> textViewTextChangeEventObservable = RxTextView.textChangeEvents(inputSearchText).debounce(400, TimeUnit.MILLISECONDS)
				.observeOn(AndroidSchedulers.mainThread());
		textViewTextChangeEventObservable.subscribe(
				new Observer<TextViewTextChangeEvent>() {
					@Override
					public void onSubscribe(Disposable d) {
						disposable = d;
					}

					@Override
					public void onNext(TextViewTextChangeEvent value) {
						search(value.text().toString());

					}

					@Override
					public void onError(Throwable e) {
						logError(e);
					}

					@Override
					public void onComplete() {

					}
				});
	}


	private void search(String keyword) {
		if (TextUtils.isEmpty(keyword)) {
			return;
		}
		showLoadingView();
		final Observable<MoviesWrapper> fetchDataObservable = AndroidApplication.getInstance().getRestClient()
				.getMovieService().searchRx(Constants.API_KEY, keyword);
		networkRequestSubscription.add(fetchDataObservable.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread()).subscribe(this::onSuccess, this::onError));
	}


	private void onSuccess(MoviesWrapper moviesWrapper) {
		List<Movie> results = moviesWrapper.getResults();
		if (results != null) {
			if (results.size() > 0) {
				movieAdapter.updateDataSet(moviesWrapper.getResults());
				hideEmptyView();
			} else {
				showEmptyView(getString(R.string.no_result));
			}
		} else {
			showEmptyView(getString(R.string.no_result));
		}

	}

	private void hideEmptyView() {
		topEmptyView.setVisibility(View.INVISIBLE);
		recyclerView.setVisibility(View.VISIBLE);
	}


	private void onError(Throwable throwable) {
		showEmptyView(getString(R.string.network_error));
		logError(throwable);
	}

	private void showEmptyView(String hint) {
		recyclerView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		topEmptyView.setVisibility(View.VISIBLE);
		tvEmptyViewHint.setText(hint);
		tvEmptyViewHint.setVisibility(View.VISIBLE);
	}


	private void showLoadingView() {
		recyclerView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
		topEmptyView.setVisibility(View.VISIBLE);
		tvEmptyViewHint.setVisibility(View.INVISIBLE);
	}

	private void logError(Throwable throwable) {
		if (BuildConfig.DEBUG) {
			Log.w(TAG, throwable);
		}
	}

	@Override
	public void onDestroyView() {

		networkRequestSubscription.dispose();
		if (disposable != null) {
			disposable.dispose();
		}
		super.onDestroyView();
	}

}