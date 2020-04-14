package nl.tudelft.trustchain.voting

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_voting.*
import nl.tudelft.ipv8.android.IPv8Android
import nl.tudelft.ipv8.attestation.trustchain.TrustChainCommunity
import nl.tudelft.trustchain.common.util.VotingHelper
import java.security.PublicKey

class VotingActivity : AppCompatActivity() {

    lateinit var vh: VotingHelper
    lateinit var community: TrustChainCommunity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_voting)

        initiateButton.setOnClickListener {
            showNewVoteDialog()
        }


        val ipv8 = IPv8Android.getInstance()
        community = ipv8.getOverlay<TrustChainCommunity>()!!
        vh = VotingHelper(community)

//
//        // option 1: download a torrent through a magnet link
//        downloadMagnetButton.setOnClickListener { _ ->
//            getMagnetLink()
//        }
//
//        // option 2: download a torrent through a .torrent file on your phone
//        downloadTorrentButton.setOnClickListener { _ ->
//            getTorrent()
//        }
//
//        // option 3: Send a message to every other peer using the superapp
//        greetPeersButton.setOnClickListener { _ ->
//            val ipv8 = IPv8Android.getInstance()
//            val demoCommunity = ipv8.getOverlay<DemoCommunity>()!!
//            val peers = demoCommunity.getPeers()
//
//            Log.i("personal", "n:" + peers.size.toString())
//            for (peer in peers) {
//                Log.i("personal", peer.mid)
//            }
//
//            demoCommunity.broadcastGreeting()
//            printToast("Greeted " + peers.size.toString() + " peers")
//        }
//
//        // option 4: dynamically load and execute code from a jar/apk file
//        executeCodeButton.setOnClickListener { _ ->
//            loadDynamicCode()
//        }
//
//        // upon launching our activity, we ask for the "Storage" permission
//        requestStoragePermission()
    }

    /**
     * Display a short message on the screen
     */
    fun printToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
    }

    /**
     * Dialog for a new proposal vote
     */
    private fun showNewVoteDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("New proposal vote")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setHint("p != np")
        builder.setView(input)

        builder.setPositiveButton("Create") { _, _ ->
            val proposal = input.text.toString()

            // Create list of your peers and include yourself
            val peers: MutableList<nl.tudelft.ipv8.keyvault.PublicKey> = ArrayList()
            peers.addAll(community.getPeers().map { it.publicKey })
            peers.add(community.myPeer.publicKey)

            // Start voting procedure
            vh.startVote(proposal, peers)
            Toast.makeText(this, "Voting procedure started", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()

    }

}
