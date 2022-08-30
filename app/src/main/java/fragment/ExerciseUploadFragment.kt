package fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.healthcare_exercise.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_exercise_upload.view.*
import java.text.SimpleDateFormat
import java.util.*


class ExerciseUploadFragment : Fragment() {

    private var viewProfile : View?=null
    var fbStorage : FirebaseStorage?=null
    var uri : Uri? = null
    lateinit var name: String
    lateinit var email: String
    var method = "unselected"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewProfile = inflater.inflate(R.layout.fragment_exercise_upload, container, false)

        fbStorage = FirebaseStorage.getInstance()

        //사용자 정보 text set
        infoSet()

        //스피너 구현
        var methods = resources.getStringArray(R.array.methods)
        var adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, methods)
        this.viewProfile!!.spinner.adapter = adapter

        this.viewProfile!!.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                method = methods.get(position)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //storage에 동영상 upload
        this.viewProfile!!.btn_analyse.setOnClickListener(View.OnClickListener { funImageUpload(viewProfile!!) })

        //realtime database에 info upload?? 필요??

        return viewProfile
    }

    private fun infoSet() {
        name = arguments?.getString("name").toString()
        email = arguments?.getString("email").toString()
        val str_uri = arguments?.getString("uri")
        uri = Uri.parse(str_uri)
        var timeStamp = SimpleDateFormat("yyMMdd_HH:mm").format(Date())
        this.viewProfile!!.video_view.setVideoURI(uri)
        this.viewProfile!!.video_view.start()
        this.viewProfile!!.tx_info.text = "E-mail: "+email+"\nUser: "+name+"\nDate: "+timeStamp
    }

    private fun funImageUpload(view:View){
        var timeStamp = SimpleDateFormat("yyMMdd_HH:mm").format(Date())
        var imgFileName = "VIDEO_"+timeStamp+"_.mp4"
        var storageRef = fbStorage?.reference?.child(email)?.child(method)?.child(imgFileName)

        storageRef?.putFile(uri!!)?.addOnSuccessListener{
            Toast.makeText(context,"Video Uploaded_"+name+"_"+method, Toast.LENGTH_LONG).show()
        }
    }
}