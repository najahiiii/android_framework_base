/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.telecom.VideoProfile;
import android.util.Log;

import com.android.internal.telephony.PhoneConstants;

/**
 * Parcelable object to handle IMS call profile.
 * It is created from GSMA IR.92/IR.94, 3GPP TS 24.229/TS 26.114/TS26.111.
 * It provides the service and call type, the additional information related to the call.
 *
 * @hide
 */
@SystemApi
public final class ImsCallProfile implements Parcelable {
    private static final String TAG = "ImsCallProfile";

    /**
     * Service types
     */
    /**
     * It is for a special case. It helps that the application can make a call
     * without IMS connection (not registered).
     * In the moment of the call initiation, the device try to connect to the IMS network
     * and initiates the call.
     */
    public static final int SERVICE_TYPE_NONE = 0;
    /**
     * It is a default type and can be selected when the device is connected to the IMS network.
     */
    public static final int SERVICE_TYPE_NORMAL = 1;
    /**
     * It is for an emergency call.
     */
    public static final int SERVICE_TYPE_EMERGENCY = 2;

    /**
     * Call types
     */
    /**
     * IMSPhone to support IR.92 & IR.94 (voice + video upgrade/downgrade)
     */
    public static final int CALL_TYPE_VOICE_N_VIDEO = 1;
    /**
     * IR.92 (Voice only)
     */
    public static final int CALL_TYPE_VOICE = 2;
    /**
     * VT to support IR.92 & IR.94 (voice + video upgrade/downgrade)
     */
    public static final int CALL_TYPE_VIDEO_N_VOICE = 3;
    /**
     * Video Telephony (audio / video two way)
     */
    public static final int CALL_TYPE_VT = 4;
    /**
     * Video Telephony (audio two way / video TX one way)
     */
    public static final int CALL_TYPE_VT_TX = 5;
    /**
     * Video Telephony (audio two way / video RX one way)
     */
    public static final int CALL_TYPE_VT_RX = 6;
    /**
     * Video Telephony (audio two way / video inactive)
     */
    public static final int CALL_TYPE_VT_NODIR = 7;
    /**
     * VideoShare (video two way)
     */
    public static final int CALL_TYPE_VS = 8;
    /**
     * VideoShare (video TX one way)
     */
    public static final int CALL_TYPE_VS_TX = 9;
    /**
     * VideoShare (video RX one way)
     */
    public static final int CALL_TYPE_VS_RX = 10;

    /**
     * Extra properties for IMS call.
     */
    /**
     * Boolean extra properties - "true" / "false"
     *  conference : Indicates if the session is for the conference call or not.
     *  e_call : Indicates if the session is for the emergency call or not.
     *  vms : Indicates if the session is connected to the voice mail system or not.
     *  call_mode_changeable : Indicates if the session is able to upgrade/downgrade
     *      the video during voice call.
     *  conference_avail : Indicates if the session can be extended to the conference.
     */
    /**
     * @hide
     */
    public static final String EXTRA_CONFERENCE = "conference";
    /**
     * @hide
     */
    public static final String EXTRA_E_CALL = "e_call";
    /**
     * @hide
     */
    public static final String EXTRA_VMS = "vms";
    /**
     * @hide
     */
    public static final String EXTRA_CALL_MODE_CHANGEABLE = "call_mode_changeable";
    /**
     * @hide
     */
    public static final String EXTRA_CONFERENCE_AVAIL = "conference_avail";

    // Extra string for internal use only. OEMs should not use
    // this for packing extras.
    /**
     * @hide
     */
    public static final String EXTRA_OEM_EXTRAS = "OemCallExtras";

    /**
     * Rule for originating identity (number) presentation, MO/MT.
     *      {@link ImsCallProfile#OIR_DEFAULT}
     *      {@link ImsCallProfile#OIR_PRESENTATION_RESTRICTED}
     *      {@link ImsCallProfile#OIR_PRESENTATION_NOT_RESTRICTED}
     */
    public static final String EXTRA_OIR = "oir";
    /**
     * Rule for calling name presentation
     *      {@link ImsCallProfile#OIR_DEFAULT}
     *      {@link ImsCallProfile#OIR_PRESENTATION_RESTRICTED}
     *      {@link ImsCallProfile#OIR_PRESENTATION_NOT_RESTRICTED}
     */
    public static final String EXTRA_CNAP = "cnap";
    /**
     * To identify the Ims call type, MO
     *      {@link ImsCallProfile#DIALSTRING_NORMAL}
     *      {@link ImsCallProfile#DIALSTRING_SS_CONF}
     *      {@link ImsCallProfile#DIALSTRING_USSD}
     */
    public static final String EXTRA_DIALSTRING = "dialstring";

    /**
     * Values for EXTRA_OIR / EXTRA_CNAP
     */
    /**
     * Default presentation for Originating Identity.
     */
    public static final int OIR_DEFAULT = 0;    // "user subscription default value"
    /**
     * Restricted presentation for Originating Identity.
     */
    public static final int OIR_PRESENTATION_RESTRICTED = 1;
    /**
     * Not restricted presentation for Originating Identity.
     */
    public static final int OIR_PRESENTATION_NOT_RESTRICTED = 2;
    /**
     * Presentation unknown for Originating Identity.
     */
    public static final int OIR_PRESENTATION_UNKNOWN = 3;
    /**
     * Payphone presentation for Originating Identity.
     */
    public static final int OIR_PRESENTATION_PAYPHONE = 4;

    //Values for EXTRA_DIALSTRING
    /**
     * A default or normal normal call.
     */
    public static final int DIALSTRING_NORMAL = 0;
    /**
     * Call for SIP-based user configuration
     */
    public static final int DIALSTRING_SS_CONF = 1;
    /**
     * Call for USSD message
     */
    public static final int DIALSTRING_USSD = 2;

    /**
     * Values for causes that restrict call types
     */
    // Default cause not restricted at peer and HD is supported
    public static final int CALL_RESTRICT_CAUSE_NONE = 0;
    // Service not supported by RAT at peer
    public static final int CALL_RESTRICT_CAUSE_RAT = 1;
    // Service Disabled at peer
    public static final int CALL_RESTRICT_CAUSE_DISABLED = 2;
    // HD is not supported
    public static final int CALL_RESTRICT_CAUSE_HD = 3;

    /**
     * String extra properties
     *  oi : Originating identity (number), MT only
     *  cna : Calling name
     *  ussd : For network-initiated USSD, MT only
     *  remote_uri : Connected user identity (it can be used for the conference)
     *  ChildNum: Child number info.
     *  Codec: Codec info.
     *  DisplayText: Display text for the call.
     *  AdditionalCallInfo: Additional call info.
     *  CallPull: Boolean value specifying if the call is a pulled call.
     */
    public static final String EXTRA_OI = "oi";
    public static final String EXTRA_CNA = "cna";
    public static final String EXTRA_USSD = "ussd";
    public static final String EXTRA_REMOTE_URI = "remote_uri";
    public static final String EXTRA_CHILD_NUMBER = "ChildNum";
    public static final String EXTRA_CODEC = "Codec";
    public static final String EXTRA_DISPLAY_TEXT = "DisplayText";
    public static final String EXTRA_ADDITIONAL_CALL_INFO = "AdditionalCallInfo";
    public static final String EXTRA_IS_CALL_PULL = "CallPull";

    /**
     * Extra key which the RIL can use to indicate the radio technology used for a call.
     * Valid values are:
     * {@link android.telephony.ServiceState#RIL_RADIO_TECHNOLOGY_LTE},
     * {@link android.telephony.ServiceState#RIL_RADIO_TECHNOLOGY_IWLAN}, and the other defined
     * {@code RIL_RADIO_TECHNOLOGY_*} constants.
     * Note: Despite the fact the {@link android.telephony.ServiceState} values are integer
     * constants, the values passed for the {@link #EXTRA_CALL_RAT_TYPE} should be strings (e.g.
     * "14" vs (int) 14).
     * Note: This is used by {@link com.android.internal.telephony.imsphone.ImsPhoneConnection#
     *      updateWifiStateFromExtras(Bundle)} to determine whether to set the
     * {@link android.telecom.Connection#PROPERTY_WIFI} property on a connection.
     */
    public static final String EXTRA_CALL_RAT_TYPE = "CallRadioTech";

    /**
     * Similar to {@link #EXTRA_CALL_RAT_TYPE}, except with a lowercase 'c'.  Used to ensure
     * compatibility with modems that are non-compliant with the {@link #EXTRA_CALL_RAT_TYPE}
     * extra key.  Should be removed when the non-compliant modems are fixed.
     * @hide
     */
    public static final String EXTRA_CALL_RAT_TYPE_ALT = "callRadioTech";

    /** @hide */
    public int mServiceType;
    /** @hide */
    public int mCallType;
    /** @hide */
    public int mRestrictCause = CALL_RESTRICT_CAUSE_NONE;

    /**
     * Extras associated with this {@link ImsCallProfile}.
     * <p>
     * Valid data types include:
     * <ul>
     *     <li>{@link Integer} (and int)</li>
     *     <li>{@link Long} (and long)</li>
     *     <li>{@link Double} (and double)</li>
     *     <li>{@link String}</li>
     *     <li>{@code int[]}</li>
     *     <li>{@code long[]}</li>
     *     <li>{@code double[]}</li>
     *     <li>{@code String[]}</li>
     *     <li>{@link PersistableBundle}</li>
     *     <li>{@link Boolean} (and boolean)</li>
     *     <li>{@code boolean[]}</li>
     *     <li>Other {@link Parcelable} classes in the {@code android.*} namespace.</li>
     * </ul>
     * <p>
     * Invalid types will be removed when the {@link ImsCallProfile} is parceled for transmit across
     * a {@link android.os.Binder}.
     */
    /** @hide */
    public Bundle mCallExtras;
    /** @hide */
    public ImsStreamMediaProfile mMediaProfile;

    /** @hide */
    public ImsCallProfile(Parcel in) {
        readFromParcel(in);
    }

    /** @hide */
    public ImsCallProfile() {
        mServiceType = SERVICE_TYPE_NORMAL;
        mCallType = CALL_TYPE_VOICE_N_VIDEO;
        mCallExtras = new Bundle();
        mMediaProfile = new ImsStreamMediaProfile();
    }

    /** @hide */
    public ImsCallProfile(int serviceType, int callType) {
        mServiceType = serviceType;
        mCallType = callType;
        mCallExtras = new Bundle();
        mMediaProfile = new ImsStreamMediaProfile();
    }

    public String getCallExtra(String name) {
        return getCallExtra(name, "");
    }

    public String getCallExtra(String name, String defaultValue) {
        if (mCallExtras == null) {
            return defaultValue;
        }

        return mCallExtras.getString(name, defaultValue);
    }

    public boolean getCallExtraBoolean(String name) {
        return getCallExtraBoolean(name, false);
    }

    public boolean getCallExtraBoolean(String name, boolean defaultValue) {
        if (mCallExtras == null) {
            return defaultValue;
        }

        return mCallExtras.getBoolean(name, defaultValue);
    }

    public int getCallExtraInt(String name) {
        return getCallExtraInt(name, -1);
    }

    public int getCallExtraInt(String name, int defaultValue) {
        if (mCallExtras == null) {
            return defaultValue;
        }

        return mCallExtras.getInt(name, defaultValue);
    }

    public void setCallExtra(String name, String value) {
        if (mCallExtras != null) {
            mCallExtras.putString(name, value);
        }
    }

    public void setCallExtraBoolean(String name, boolean value) {
        if (mCallExtras != null) {
            mCallExtras.putBoolean(name, value);
        }
    }

    public void setCallExtraInt(String name, int value) {
        if (mCallExtras != null) {
            mCallExtras.putInt(name, value);
        }
    }

    public void updateCallType(ImsCallProfile profile) {
        mCallType = profile.mCallType;
    }

    public void updateCallExtras(ImsCallProfile profile) {
        mCallExtras.clear();
        mCallExtras = (Bundle) profile.mCallExtras.clone();
    }

    @Override
    public String toString() {
        return "{ serviceType=" + mServiceType +
                ", callType=" + mCallType +
                ", restrictCause=" + mRestrictCause +
                ", mediaProfile=" + mMediaProfile.toString() + " }";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        Bundle filteredExtras = maybeCleanseExtras(mCallExtras);
        out.writeInt(mServiceType);
        out.writeInt(mCallType);
        out.writeBundle(filteredExtras);
        out.writeParcelable(mMediaProfile, 0);
    }

    private void readFromParcel(Parcel in) {
        mServiceType = in.readInt();
        mCallType = in.readInt();
        mCallExtras = in.readBundle();
        mMediaProfile = in.readParcelable(ImsStreamMediaProfile.class.getClassLoader());
    }

    public static final Creator<ImsCallProfile> CREATOR = new Creator<ImsCallProfile>() {
        @Override
        public ImsCallProfile createFromParcel(Parcel in) {
            return new ImsCallProfile(in);
        }

        @Override
        public ImsCallProfile[] newArray(int size) {
            return new ImsCallProfile[size];
        }
    };

    public int getServiceType() {
        return mServiceType;
    }

    public int getCallType() {
        return mCallType;
    }

    public int getRestrictCause() {
        return mRestrictCause;
    }

    public Bundle getCallExtras() {
        return mCallExtras;
    }

    public ImsStreamMediaProfile getMediaProfile() {
        return mMediaProfile;
    }

    /**
     * Converts from the call types defined in {@link ImsCallProfile} to the
     * video state values defined in {@link VideoProfile}.
     *
     * @param callProfile The call profile.
     * @return The video state.
     */
    public static int getVideoStateFromImsCallProfile(ImsCallProfile callProfile) {
        int videostate = getVideoStateFromCallType(callProfile.mCallType);
        if (callProfile.isVideoPaused() && !VideoProfile.isAudioOnly(videostate)) {
            videostate |= VideoProfile.STATE_PAUSED;
        } else {
            videostate &= ~VideoProfile.STATE_PAUSED;
        }
        return videostate;
    }

    /**
     * Translates a {@link ImsCallProfile} {@code CALL_TYPE_*} constant into a video state.
     * @param callType The call type.
     * @return The video state.
     */
    public static int getVideoStateFromCallType(int callType) {
        int videostate = VideoProfile.STATE_AUDIO_ONLY;
        switch (callType) {
            case CALL_TYPE_VT_TX:
                videostate = VideoProfile.STATE_TX_ENABLED;
                break;
            case CALL_TYPE_VT_RX:
                videostate = VideoProfile.STATE_RX_ENABLED;
                break;
            case CALL_TYPE_VT:
                videostate = VideoProfile.STATE_BIDIRECTIONAL;
                break;
            case CALL_TYPE_VOICE:
                videostate = VideoProfile.STATE_AUDIO_ONLY;
                break;
            default:
                videostate = VideoProfile.STATE_AUDIO_ONLY;
                break;
        }
        return videostate;
    }

    /**
     * Converts from the video state values defined in {@link VideoProfile}
     * to the call types defined in {@link ImsCallProfile}.
     *
     * @param videoState The video state.
     * @return The call type.
     */
    public static int getCallTypeFromVideoState(int videoState) {
        boolean videoTx = isVideoStateSet(videoState, VideoProfile.STATE_TX_ENABLED);
        boolean videoRx = isVideoStateSet(videoState, VideoProfile.STATE_RX_ENABLED);
        boolean isPaused = isVideoStateSet(videoState, VideoProfile.STATE_PAUSED);
        if (isPaused) {
            return ImsCallProfile.CALL_TYPE_VT_NODIR;
        } else if (videoTx && !videoRx) {
            return ImsCallProfile.CALL_TYPE_VT_TX;
        } else if (!videoTx && videoRx) {
            return ImsCallProfile.CALL_TYPE_VT_RX;
        } else if (videoTx && videoRx) {
            return ImsCallProfile.CALL_TYPE_VT;
        }
        return ImsCallProfile.CALL_TYPE_VOICE;
    }

    /**
     * Badly named old method, kept for compatibility.
     * See {@link #presentationToOir(int)}.
     * @hide
     */
    public static int presentationToOIR(int presentation) {
        switch (presentation) {
            case PhoneConstants.PRESENTATION_RESTRICTED:
                return ImsCallProfile.OIR_PRESENTATION_RESTRICTED;
            case PhoneConstants.PRESENTATION_ALLOWED:
                return ImsCallProfile.OIR_PRESENTATION_NOT_RESTRICTED;
            case PhoneConstants.PRESENTATION_PAYPHONE:
                return ImsCallProfile.OIR_PRESENTATION_PAYPHONE;
            case PhoneConstants.PRESENTATION_UNKNOWN:
                return ImsCallProfile.OIR_PRESENTATION_UNKNOWN;
            default:
                return ImsCallProfile.OIR_DEFAULT;
        }
    }

    /**
     * Translate presentation value to OIR value
     * @param presentation
     * @return OIR values
     */
    public static int presentationToOir(int presentation) {
        return presentationToOIR(presentation);
    }

    /**
     * Translate OIR value to presentation value
     * @param oir value
     * @return presentation value
     * @hide
     */
    public static int OIRToPresentation(int oir) {
        switch(oir) {
            case ImsCallProfile.OIR_PRESENTATION_RESTRICTED:
                return PhoneConstants.PRESENTATION_RESTRICTED;
            case ImsCallProfile.OIR_PRESENTATION_NOT_RESTRICTED:
                return PhoneConstants.PRESENTATION_ALLOWED;
            case ImsCallProfile.OIR_PRESENTATION_PAYPHONE:
                return PhoneConstants.PRESENTATION_PAYPHONE;
            case ImsCallProfile.OIR_PRESENTATION_UNKNOWN:
                return PhoneConstants.PRESENTATION_UNKNOWN;
            default:
                return PhoneConstants.PRESENTATION_UNKNOWN;
        }
    }

    /**
     * Checks if video call is paused
     * @return true if call is video paused
     */
    public boolean isVideoPaused() {
        return mMediaProfile.mVideoDirection == ImsStreamMediaProfile.DIRECTION_INACTIVE;
    }

    /**
     * Determines if the {@link ImsCallProfile} represents a video call.
     *
     * @return {@code true} if the profile is for a video call, {@code false} otherwise.
     */
    public boolean isVideoCall() {
        return VideoProfile.isVideo(getVideoStateFromCallType(mCallType));
    }

    /**
     * Cleanses a {@link Bundle} to ensure that it contains only data of type:
     * 1. Primitive data types (e.g. int, bool, and other values determined by
     * {@link android.os.PersistableBundle#isValidType(Object)}).
     * 2. Other Bundles.
     * 3. {@link Parcelable} objects in the {@code android.*} namespace.
     * @param extras the source {@link Bundle}
     * @return where all elements are valid types the source {@link Bundle} is returned unmodified,
     *      otherwise a copy of the {@link Bundle} with the invalid elements is returned.
     */
    private Bundle maybeCleanseExtras(Bundle extras) {
        if (extras == null) {
            return null;
        }

        int startSize = extras.size();
        Bundle filtered = extras.filterValues();
        int endSize = filtered.size();
        if (startSize != endSize) {
            Log.i(TAG, "maybeCleanseExtras: " + (startSize - endSize) + " extra values were "
                    + "removed - only primitive types and system parcelables are permitted.");
        }
        return filtered;
    }

    /**
     * Determines if a video state is set in a video state bit-mask.
     *
     * @param videoState The video state bit mask.
     * @param videoStateToCheck The particular video state to check.
     * @return True if the video state is set in the bit-mask.
     */
    private static boolean isVideoStateSet(int videoState, int videoStateToCheck) {
        return (videoState & videoStateToCheck) == videoStateToCheck;
    }
}