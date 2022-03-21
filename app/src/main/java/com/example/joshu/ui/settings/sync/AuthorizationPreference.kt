package com.example.joshu.ui.settings.sync

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.example.joshu.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AuthorizationPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    var interaction: IAuthInteraction? = null

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val view = holder?.itemView

        val googleInputLayout = view?.findViewById<TextInputLayout>(R.id.googleAuthView)
        if (googleInputLayout is TextInputLayout) {
            googleInputLayout.hint = context.getString(R.string.authorization_with_google)
            googleInputLayout.startIconDrawable = context.getDrawable(R.drawable.ic_google)
            googleInputLayout.setStartIconTintList(null)
            val inputEdit: TextInputEditText = googleInputLayout.findViewById(R.id.editTextView)
            inputEdit.isFocusableInTouchMode = false
            inputEdit.isLongClickable = false
            inputEdit.isClickable = true
            inputEdit.setOnClickListener {
                interaction?.loginWithGoogle()
            }
        }

        val facebookInputLayout = view?.findViewById<TextInputLayout>(R.id.facebookAuthView)
        if (facebookInputLayout is TextInputLayout) {
            facebookInputLayout.hint = context.getString(R.string.authorization_with_facebook)
            facebookInputLayout.startIconDrawable = context.getDrawable(R.drawable.ic_facebook)
            facebookInputLayout.setStartIconTintList(null)
            val inputEdit: TextInputEditText = facebookInputLayout.findViewById(R.id.editTextView)
            inputEdit.isFocusableInTouchMode = false
            inputEdit.isLongClickable = false
            inputEdit.isClickable = true
            inputEdit.setOnClickListener {
                interaction?.loginWithFacebook()
            }
        }

        val vkInputLayout = view?.findViewById<TextInputLayout>(R.id.vkAuthView)
        if (vkInputLayout is TextInputLayout) {
            vkInputLayout.hint = context.getString(R.string.authorization_with_vk)
            vkInputLayout.startIconDrawable = context.getDrawable(R.drawable.ic24_vk)
            vkInputLayout.setStartIconTintList(null)
            val inputEdit: TextInputEditText = vkInputLayout.findViewById(R.id.editTextView)
            inputEdit.isFocusableInTouchMode = false
            inputEdit.isLongClickable = false
            inputEdit.isClickable = true
            inputEdit.setOnClickListener {
                interaction?.loginWithVK()
            }
        }

        val emailInputLayout = view?.findViewById<TextInputLayout>(R.id.emailInputLayoutView)
        if (emailInputLayout is TextInputLayout) {
            emailInputLayout.hint = context.getString(R.string.enter_address_email_hint)
            val emailInputEdit: TextInputEditText =
                emailInputLayout.findViewById(R.id.editTextView)
            emailInputEdit.inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        val passwordInputLayout = view?.findViewById<TextInputLayout>(R.id.passwordInputLayoutView)
        if (passwordInputLayout is TextInputLayout) {
            passwordInputLayout.hint = context.getString(R.string.enter_your_password)
            passwordInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE

            val passwordInputEdit: TextInputEditText =
                passwordInputLayout.findViewById(R.id.editTextView)
            passwordInputEdit.inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val loginBtn = view?.findViewById<MaterialButton>(R.id.actionBtnView)
        if (loginBtn is MaterialButton) {
            loginBtn.setText(R.string.sign_in)
        }
        loginBtn?.setOnClickListener {
            interaction?.loginWithEmail()
        }

        val forgotPasswordBtn = view?.findViewById<TextView>(R.id.forgotPasswordView)
        forgotPasswordBtn?.setOnClickListener {
            interaction?.forgotPassword()
        }

    }
}