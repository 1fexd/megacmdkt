package fe.megacmdkt

import fe.megacmdkt.auth.MegaAuth
import fe.megacmdkt.cmd.*
import fe.megacmdkt.cmd.mega.*
import java.util.function.Predicate

/**
 * Facade of the MEGAcmd functions that works with an existing session.
 *
 * For all those functions
 * that return an instance of AbstractMegaCmd remember to use a [Callable.call] ()} if it is
 * supposed to return something or [Runnable.run] if it doesn't.
 */
class MegaSession(authentication: MegaAuth) {
    private val authentication: MegaAuth

    init {
        this.authentication = authentication
    }

    /**
     * Retrieve the existing authentication object for this session
     *
     * @return [MegaAuth] not null
     */
    fun getAuthentication(): MegaAuth {
        return authentication
    }

    /**
     * Changes the password of the current logged user
     *
     * @param oldPassword [String] with the current password
     * @param newPassword [String] having the new password
     */
    fun changePassword(oldPassword: String, newPassword: String) {
        MegaCmdChangePassword(oldPassword, newPassword).run()
        System.setProperty(Mega.PASSWORD_ENV_VAR, newPassword)
    }

    /**
     * Closes de current session
     */
    fun logout() {
        MegaCmdLogout().run()
    }

    /**
     * Returns the ID to identify the session in MEGA
     *
     * @return [String] not null
     */
    fun sessionID(): String {
        return MegaCmdSession().call()
    }

    /**
     * Returns the username/email of the current session
     *
     * @return [String] not null
     */
    fun whoAmI(): String {
        return MegaCmdWhoAmI().call()
    }

    /**
     * Uploads a single file/folder using the current remote working directory as destination for the
     * upload.
     *
     * @param localFilePath [String] with the path of the file/folder to upload
     * @return [MegaCmdPutSingle] to be configured and run
     * @see .uploadFile
     * @see .uploadFiles
     */
    fun uploadFile(localFilePath: String): MegaCmdPutSingle {
        return MegaCmdPutSingle(localFilePath)
    }

    /**
     * Uploads a single file/folder to a remote folder.
     *
     * @param localFilePath [String] with the path of the file/folder to upload
     * @param remotePath [String] of the remotePath where upload the content of `localFilePath`
     * @return [MegaCmdPutSingle] to be configured and run
     * @see .uploadFile
     * @see .uploadFiles
     */
    fun uploadFile(localFilePath: String, remotePath: String): MegaCmdPutSingle {
        return MegaCmdPutSingle(localFilePath, remotePath)
    }

    /**
     * Uploads multiple files/folders to a remote folder
     *
     * @param remotePath [String] with the remote path where to upload the files/folders
     * @param localFilesNames [String[]] with local files/folders to be uploaded
     * @return [MegaCmdPutMultiple] to be configured and run
     * @see .uploadFile
     * @see .uploadFile
     */
    fun uploadFiles(
        remotePath: String, vararg localFilesNames: String
    ): MegaCmdPutMultiple {
        return MegaCmdPutMultiple(remotePath, *localFilesNames)
    }

    /**
     * Creates a directory or a directories hierarchy
     *
     * @param remotePath [String] with the path of the directory to create in MEGA
     * @return [MegaCmdMakeDirectory] to be configured and run.
     */
    fun makeDirectory(remotePath: String): MegaCmdMakeDirectory {
        return MegaCmdMakeDirectory(remotePath)
    }

    /**
     * Copies a remote file/folder into a another remote location. to be configured and run. If the
     * `remoteTarget` exists and is a folder, the source will be copied there If the location
     * doesn't exits, the file/folder will be renamed to `remoteTarget`.
     *
     * @param remoteSourcePath [String] with remote path of the source file/folder
     * @param remoteTarget [String] with remote path where the file/folder will be copied
     * @return [MegaCmdCopy] to be configured and run.
     */
    fun copy(remoteSourcePath: String, remoteTarget: String): MegaCmdCopy {
        return MegaCmdCopy(remoteSourcePath, remoteTarget)
    }

    /**
     * Moves a file/folder into a new location (all remotes)
     *
     * @param remoteSourcePath [String] with remote path of the source file/folder
     * @param remoteTarget with remote path where the file/folder will be moved
     * @return [MegaCmdMove] to be configured and run.
     */
    fun move(remoteSourcePath: String, remoteTarget: String): MegaCmdMove {
        return MegaCmdMove(remoteSourcePath, remoteTarget)
    }

    /**
     * Lists files in a remote path. The param `remotepath` can be a pattern (it accepts
     * wildcards: ? and *. e.g.: f*00?.txt). Also, constructions like /PATTERN1/PATTERN2/PATTERN3 are
     * allowed
     *
     * @param remotePath [String] with the remote path to be listed
     * @return [MegaCmdList] to be configured and called
     */
    fun ls(remotePath: String): MegaCmdList {
        return MegaCmdList(remotePath)
    }

    /**
     * Downloads a remote file/folder or a public link to the current folder. For folders, the entire
     * contents (and the root folder itself) will be by default downloaded.
     *
     * @param remotePath [String] with the remote path of the file/folder to be downloaded
     * @return [MegaCmdGet] to be configured and run.
     * @see .get
     */
    operator fun get(remotePath: String): MegaCmdGet {
        return MegaCmdGet(remotePath)
    }

    /**
     * Downloads a remote file/folder or a public link to `localPath`. For folders, the entire
     * contents (and the root folder itself) will be by default downloaded.
     *
     * @param remotePath [String] with the remote path of the file/folder to be downloaded
     * @param localPath [String] with the local path where to put the downloaded file/folder
     * @return [MegaCmdGet] to be configured and run.
     * @see .get
     */
    operator fun get(remotePath: String, localPath: String): MegaCmdGet {
        return MegaCmdGet(remotePath, localPath)
    }

    /**
     * Removes a file in the specified (MEGA) remote path. Does not includes directories
     *
     * @param remotePath [String] with the remote path of the file to remove
     * @return [MegaCmdRemove] not null
     * @see .removeDirectory
     */
    fun remove(remotePath: String): MegaCmdRemove {
        return MegaCmdRemove(remotePath)
    }

    /**
     * This allows to delete directories in the specified (MEGA) remote path.
     *
     * @param remotePath [String] with the remote path of the directory to remove
     * @return [MegaCmdRemove] not null
     */
    fun removeDirectory(remotePath: String): MegaCmdRemove {
        return MegaCmdRemove(remotePath).deleteRecursively()
    }

    /**
     * Count the amount of elements in a remote path. Includes folders and files
     *
     * @param remotePath [String] with the remote path
     * @return [Long] with the amount of elements
     * @see .count
     */
    fun count(remotePath: String): Int {
        return ls(remotePath).count()
    }

    /**
     * Count the amount of elements in a remote path, filtering by a Predicate.
     *
     * @param remotePath [String] with the remote path
     * @param predicate [Predicate] that will filter the count of elements to have in
     * count
     * @return [Long] with the amount of elements
     * @see .count
     */
    fun count(remotePath: String, predicate: (FileInfo) -> Boolean): Int {
        return ls(remotePath).count(predicate)
    }

    fun exists(remotePath: String): Boolean {
        return ls(remotePath).exists()
    }

    /**
     * Shares a resource hosted in Mega with a given user, giving him an specific
     * MegaCmdShare.AccessLevel.
     *
     * @param remotePath [String] with the remote path of the resource in MEGA to share
     * @param userMailToShareWith [String] with the username of the user to share the resource
     * with
     * @return [MegaCmdShare] not null
     * @see .export
     */
    fun share(remotePath: String, userMailToShareWith: String): MegaCmdShare {
        return MegaCmdShare(remotePath, userMailToShareWith)
    }

    /**
     * Exports a remote resource located in MEGA to be used by a public yet secret url
     *
     * @param remotePath [String] with the remote path of the resource to be exported
     * @return [MegaCmdExport] not null
     * @see .share
     */
    fun export(remotePath: String): MegaCmdExport {
        return MegaCmdExport(remotePath)
    }

    /**
     * Enables HTTPS for transactions
     *
     * @return [Boolean] with true|false if it ran successfully or not
     */
    fun enableHttps(): Boolean {
        return MegaCmdHttps(true).call()
    }

    /**
     * Disables HTTPS for transactions
     *
     * @return [Boolean] with false|true if it ran successfully or not
     */
    fun disableHttps(): Boolean {
        return MegaCmdHttps(false).call()
    }

    /**
     * Requests if HTTPS is used for transactions
     *
     * @return [Boolean] with true|false if HTTPS is used for transactions or not
     */
    val isHttpsEnabled by lazy { MegaCmdHttps().call() }


    /**
     * import exported link into your cloud
     *
     * @param exportedLink [String] the link to be imported
     * @return [MegaCmdImport] to be configured and run.
     */
    fun importLink(exportedLink: String): MegaCmdImport {
        return MegaCmdImport(exportedLink)
    }
}
