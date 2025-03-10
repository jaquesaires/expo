package abi43_0_0.expo.modules.medialibrary;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;

import abi43_0_0.expo.modules.core.ExportedModule;
import abi43_0_0.expo.modules.core.ModuleRegistry;
import abi43_0_0.expo.modules.core.Promise;
import abi43_0_0.expo.modules.core.interfaces.ActivityEventListener;
import abi43_0_0.expo.modules.core.interfaces.ActivityProvider;
import abi43_0_0.expo.modules.core.interfaces.ExpoMethod;
import abi43_0_0.expo.modules.core.interfaces.services.EventEmitter;
import abi43_0_0.expo.modules.core.interfaces.services.UIManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import abi43_0_0.expo.modules.interfaces.permissions.Permissions;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.ERROR_NO_ALBUM;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.ERROR_NO_PERMISSIONS;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.ERROR_NO_PERMISSIONS_MESSAGE;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.ERROR_NO_WRITE_PERMISSION_MESSAGE;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.ERROR_UNABLE_TO_ASK_FOR_PERMISSIONS;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.ERROR_UNABLE_TO_ASK_FOR_PERMISSIONS_MESSAGE;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.ERROR_USER_DID_NOT_GRANT_WRITE_PERMISSIONS_MESSAGE;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.EXTERNAL_CONTENT;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.LIBRARY_DID_CHANGE_EVENT;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.MEDIA_TYPE_ALL;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.MEDIA_TYPE_AUDIO;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.MEDIA_TYPE_PHOTO;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.MEDIA_TYPE_UNKNOWN;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.MEDIA_TYPE_VIDEO;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.SORT_BY_CREATION_TIME;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.SORT_BY_DEFAULT;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.SORT_BY_DURATION;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.SORT_BY_HEIGHT;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.SORT_BY_MEDIA_TYPE;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.SORT_BY_MODIFICATION_TIME;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryConstants.SORT_BY_WIDTH;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryUtils.getAssetsById;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryUtils.getAssetsInAlbums;
import static abi43_0_0.expo.modules.medialibrary.MediaLibraryUtils.getAssetsUris;


public class MediaLibraryModule extends ExportedModule implements ActivityEventListener {

  private static final int WRITE_REQUEST_CODE = 7463;

  private MediaStoreContentObserver mImagesObserver = null;
  private MediaStoreContentObserver mVideosObserver = null;
  private final Context mContext;
  private ModuleRegistry mModuleRegistry;
  private Action mAction;

  public MediaLibraryModule(Context context) {
    super(context);
    mContext = context;
  }

  @Override
  public String getName() {
    return "ExponentMediaLibrary";
  }

  @Override
  public Map<String, Object> getConstants() {
    return Collections.unmodifiableMap(new HashMap<String, Object>() {
      {
        put("MediaType", Collections.unmodifiableMap(new HashMap<String, Object>() {
          {
            put("audio", MEDIA_TYPE_AUDIO);
            put("photo", MEDIA_TYPE_PHOTO);
            put("video", MEDIA_TYPE_VIDEO);
            put("unknown", MEDIA_TYPE_UNKNOWN);
            put("all", MEDIA_TYPE_ALL);
          }
        }));
        put("SortBy", Collections.unmodifiableMap(new HashMap<String, Object>() {
          {
            put("default", SORT_BY_DEFAULT);
            put("creationTime", SORT_BY_CREATION_TIME);
            put("modificationTime", SORT_BY_MODIFICATION_TIME);
            put("mediaType", SORT_BY_MEDIA_TYPE);
            put("width", SORT_BY_WIDTH);
            put("height", SORT_BY_HEIGHT);
            put("duration", SORT_BY_DURATION);
          }
        }));
        put("CHANGE_LISTENER_NAME", LIBRARY_DID_CHANGE_EVENT);
      }
    });
  }

  @Override
  public void onCreate(ModuleRegistry moduleRegistry) {
    mModuleRegistry = moduleRegistry;
  }

  @ExpoMethod
  public void requestPermissionsAsync(boolean writeOnly, final Promise promise) {
    Permissions.askForPermissionsWithPermissionsManager(mModuleRegistry.getModule(Permissions.class), promise, getManifestPermissions(writeOnly));
  }

  @ExpoMethod
  public void getPermissionsAsync(boolean writeOnly, final Promise promise) {
    Permissions.getPermissionsWithPermissionsManager(mModuleRegistry.getModule(Permissions.class), promise, getManifestPermissions(writeOnly));
  }

  @ExpoMethod
  public void saveToLibraryAsync(String localUri, Promise promise) {
    if (isMissingWritePermission()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_WRITE_PERMISSION_MESSAGE);
      return;
    }

    new CreateAsset(mContext, localUri, promise, false)
      .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  @ExpoMethod
  public void createAssetAsync(String localUri, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    new CreateAsset(mContext, localUri, promise)
      .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  @ExpoMethod
  public void addAssetsToAlbumAsync(List<String> assetsId, String albumId, boolean copyToAlbum, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    Action action = permissionsWereGranted -> {
      if (!permissionsWereGranted) {
        promise.reject(ERROR_NO_PERMISSIONS, ERROR_USER_DID_NOT_GRANT_WRITE_PERMISSIONS_MESSAGE);
        return;
      }

      new AddAssetsToAlbum(mContext,
        assetsId.toArray(new String[0]), albumId, copyToAlbum, promise).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    };

    runActionWithPermissions(copyToAlbum ? Collections.emptyList() : assetsId, action, promise);
  }

  @ExpoMethod
  public void removeAssetsFromAlbumAsync(List<String> assetsId, String albumId, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    Action action = permissionsWereGranted -> {
      if (!permissionsWereGranted) {
        promise.reject(ERROR_NO_PERMISSIONS, ERROR_USER_DID_NOT_GRANT_WRITE_PERMISSIONS_MESSAGE);
        return;
      }

      new RemoveAssetsFromAlbum(mContext,
        assetsId.toArray(new String[0]), albumId, promise).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    };

    runActionWithPermissions(assetsId, action, promise);
  }

  @ExpoMethod
  public void deleteAssetsAsync(List<String> assetsId, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    Action action = permissionsWereGranted -> {
      if (!permissionsWereGranted) {
        promise.reject(ERROR_NO_PERMISSIONS, ERROR_USER_DID_NOT_GRANT_WRITE_PERMISSIONS_MESSAGE);
        return;
      }

      new DeleteAssets(mContext, assetsId.toArray(new String[0]), promise)
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    };

    runActionWithPermissions(assetsId, action, promise);
  }

  @ExpoMethod
  public void getAssetInfoAsync(String assetId, Map<String, Object> options /* unused on android atm */, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    new GetAssetInfo(mContext, assetId, promise).
      executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }


  @ExpoMethod
  public void getAlbumsAsync(Map<String, Object> options /* unused on android atm */, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    new GetAlbums(mContext, promise).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }


  @ExpoMethod
  public void getAlbumAsync(String albumName, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    new GetAlbum(mContext, albumName, promise)
      .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  @ExpoMethod
  public void createAlbumAsync(String albumName, String assetId, boolean copyAsset, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    Action action = permissionsWereGranted -> {
      if (!permissionsWereGranted) {
        promise.reject(ERROR_NO_PERMISSIONS, ERROR_USER_DID_NOT_GRANT_WRITE_PERMISSIONS_MESSAGE);
        return;
      }

      new CreateAlbum(mContext, albumName, assetId, copyAsset, promise)
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    };

    runActionWithPermissions(copyAsset ? Collections.emptyList() : Collections.singletonList(assetId), action, promise);
  }

  @ExpoMethod
  public void deleteAlbumsAsync(List<String> albumIds, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    Action action = permissionsWereGranted -> {
      if (!permissionsWereGranted) {
        promise.reject(ERROR_NO_PERMISSIONS, ERROR_USER_DID_NOT_GRANT_WRITE_PERMISSIONS_MESSAGE);
        return;
      }

      new DeleteAlbums(mContext, albumIds, promise)
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    };

    runActionWithPermissions(getAssetsInAlbums(mContext, albumIds.toArray(new String[0])), action, promise);
  }

  @ExpoMethod
  public void getAssetsAsync(Map<String, Object> assetOptions, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    new GetAssets(mContext, assetOptions, promise)
      .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  @ExpoMethod
  public void migrateAlbumIfNeededAsync(String albumId, Promise promise) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
      promise.resolve(null);
      return;
    }

    List<MediaLibraryUtils.AssetFile> assets = getAssetsById(
      mContext,
      null,
      getAssetsInAlbums(mContext, albumId).toArray(new String[0])
    );

    if (assets == null) {
      promise.reject(ERROR_NO_ALBUM, "Couldn't find album.");
      return;
    }

    Map<File, List<MediaLibraryUtils.AssetFile>> albumsMap = assets
      .stream()
      // All files should have mime type, but if not, we can safely assume that
      // those without mime type shouldn't be move
      .filter(asset -> asset.getMimeType() != null)
      .collect(Collectors.groupingBy(File::getParentFile));

    if (albumsMap.size() != 1) {
      // Empty albums shouldn't be visible to users. That's why this is an error.
      promise.reject(ERROR_NO_ALBUM, "Found album is empty.");
      return;
    }

    File albumDir = assets.get(0).getParentFile();
    if (albumDir == null) {
      promise.reject(ERROR_NO_ALBUM, "Couldn't get album path.");
      return;
    }

    if (albumDir.canWrite()) {
      // Nothing to migrate
      promise.resolve(null);
      return;
    }

    List<String> needsToCheckPermissions = assets
      .stream()
      .map(MediaLibraryUtils.AssetFile::getAssetId)
      .collect(Collectors.toList());

    Action action = permissionsWereGranted -> {
      if (!permissionsWereGranted) {
        promise.reject(ERROR_NO_PERMISSIONS, ERROR_USER_DID_NOT_GRANT_WRITE_PERMISSIONS_MESSAGE);
        return;
      }

      new MigrateAlbum(mContext, assets, albumDir.getName(), promise)
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    };

    runActionWithPermissions(needsToCheckPermissions, action, promise);
  }

  @ExpoMethod
  public void albumNeedsMigrationAsync(String albumId, Promise promise) {
    if (isMissingPermissions()) {
      promise.reject(ERROR_NO_PERMISSIONS, ERROR_NO_PERMISSIONS_MESSAGE);
      return;
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      new CheckIfAlbumShouldBeMigrated(mContext, albumId, promise)
        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      return;
    }

    promise.resolve(false);
  }

  // Library change observer

  @ExpoMethod
  public void startObserving(Promise promise) {
    if (mImagesObserver != null) {
      promise.resolve(null);
      return;
    }

    // We need to register an observer for each type of assets,
    // because it seems that observing a parent directory (EXTERNAL_CONTENT) doesn't work well,
    // whereas observing directory of images or videos works fine.

    Handler handler = new Handler();
    mImagesObserver = new MediaStoreContentObserver(handler, Files.FileColumns.MEDIA_TYPE_IMAGE);
    mVideosObserver = new MediaStoreContentObserver(handler, Files.FileColumns.MEDIA_TYPE_VIDEO);

    ContentResolver contentResolver = mContext.getContentResolver();

    contentResolver.registerContentObserver(
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
      true,
      mImagesObserver
    );
    contentResolver.registerContentObserver(
      MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
      true,
      mVideosObserver
    );
    promise.resolve(null);
  }

  @ExpoMethod
  public void stopObserving(Promise promise) {
    if (mImagesObserver != null) {
      ContentResolver contentResolver = mContext.getContentResolver();

      contentResolver.unregisterContentObserver(mImagesObserver);
      contentResolver.unregisterContentObserver(mVideosObserver);

      mImagesObserver = null;
      mVideosObserver = null;
    }
    promise.resolve(null);
  }

  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    if (requestCode == WRITE_REQUEST_CODE && mAction != null) {
      mAction.runWithPermissions(resultCode == Activity.RESULT_OK);
      mAction = null;
      mModuleRegistry.getModule(UIManager.class).unregisterActivityEventListener(this);
    }
  }

  @Override
  public void onNewIntent(Intent intent) {
  }

  private boolean isMissingPermissions() {
    Permissions permissionsManager = mModuleRegistry.getModule(Permissions.class);
    if (permissionsManager == null) {
      return false;
    }

    return !permissionsManager.hasGrantedPermissions(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);
  }

  private boolean isMissingWritePermission() {
    Permissions permissionsManager = mModuleRegistry.getModule(Permissions.class);
    if (permissionsManager == null) {
      return false;
    }

    return !permissionsManager.hasGrantedPermissions(WRITE_EXTERNAL_STORAGE);
  }

  private String[] getManifestPermissions(boolean writeOnly) {
    final List<String> permissions = new ArrayList<>();
    permissions.add(WRITE_EXTERNAL_STORAGE);
    if (!writeOnly) {
      permissions.add(READ_EXTERNAL_STORAGE);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      permissions.add(ACCESS_MEDIA_LOCATION);
    }
    return permissions.toArray(new String[0]);
  }

  private void runActionWithPermissions(List<String> assetsId, Action action, Promise promise) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      List<Uri> pathsWithoutPermissions = getAssetsUris(mContext, assetsId)
        .stream()
        .filter(
          uri -> mContext.checkUriPermission(
            uri,
            Binder.getCallingPid(),
            Binder.getCallingUid(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION
          ) != PackageManager.PERMISSION_GRANTED
        )
        .collect(Collectors.toList());

      if (!pathsWithoutPermissions.isEmpty()) {
        PendingIntent deleteRequest = MediaStore.createWriteRequest(mContext.getContentResolver(), pathsWithoutPermissions);
        Activity activity = mModuleRegistry.getModule(ActivityProvider.class).getCurrentActivity();
        try {
          mModuleRegistry.getModule(UIManager.class).registerActivityEventListener(this);
          mAction = action;

          activity.startIntentSenderForResult(
            deleteRequest.getIntentSender(),
            WRITE_REQUEST_CODE,
            null,
            0,
            0,
            0
          );
        } catch (IntentSender.SendIntentException e) {
          promise.reject(ERROR_UNABLE_TO_ASK_FOR_PERMISSIONS, ERROR_UNABLE_TO_ASK_FOR_PERMISSIONS_MESSAGE);
          mAction = null;
        }

        return;
      }
    }

    action.runWithPermissions(true);
  }

  @FunctionalInterface
  interface Action {
    void runWithPermissions(boolean permissionsWereGranted);
  }

  private class MediaStoreContentObserver extends ContentObserver {
    private int mAssetsTotalCount;
    private final int mMediaType;

    public MediaStoreContentObserver(Handler handler, int mediaType) {
      super(handler);
      mMediaType = mediaType;
      mAssetsTotalCount = getAssetsTotalCount(mMediaType);
    }

    @Override
    public void onChange(boolean selfChange) {
      this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
      int newTotalCount = getAssetsTotalCount(mMediaType);

      // Send event to JS only when assets count has been changed - to filter out some unnecessary events.
      // It's not perfect solution if someone adds and deletes the same number of assets in a short period of time, but I hope these events will not be batched.
      if (mAssetsTotalCount != newTotalCount) {
        mAssetsTotalCount = newTotalCount;
        mModuleRegistry.getModule(EventEmitter.class).emit(LIBRARY_DID_CHANGE_EVENT, new Bundle());
      }
    }

    private int getAssetsTotalCount(int mediaType) {
      try (Cursor countCursor = mContext.getContentResolver().query(
        EXTERNAL_CONTENT,
        null,
        Files.FileColumns.MEDIA_TYPE + " == " + mediaType,
        null,
        null
      )) {
        return countCursor != null ? countCursor.getCount() : 0;
      }
    }
  }
}
