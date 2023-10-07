package com.capco.hackathon2023;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;

import java.util.ArrayList;
import java.util.List;

class LogAnalysis {
    private static final String AZURE_OPENAI_API_KEY = "accbec4a886f4c9c9dfafc861931841f";
    private static final String AZURE_OPENAI_LANGUAGE_ENDPOINT = "https://log-analysis-resource.openai.azure.com/";

    private static final String LOG_EXAMPLE =
            "Dec 10 07:28:10 LabSZ sshd[24251]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:12 LabSZ sshd[24251]: Failed password for root from 112.95.230.3 port 58304 ssh2\n" +
                    "Dec 10 07:28:12 LabSZ sshd[24251]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:12 LabSZ sshd[24253]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:14 LabSZ sshd[24253]: Failed password for root from 112.95.230.3 port 59849 ssh2\n" +
                    "Dec 10 07:28:14 LabSZ sshd[24253]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:14 LabSZ sshd[24255]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:16 LabSZ sshd[24255]: Failed password for root from 112.95.230.3 port 32977 ssh2\n" +
                    "Dec 10 07:28:16 LabSZ sshd[24255]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:16 LabSZ sshd[24257]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:18 LabSZ sshd[24257]: Failed password for root from 112.95.230.3 port 35113 ssh2\n" +
                    "Dec 10 07:28:18 LabSZ sshd[24257]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:19 LabSZ sshd[24259]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:21 LabSZ sshd[24259]: Failed password for root from 112.95.230.3 port 37035 ssh2\n" +
                    "Dec 10 07:28:21 LabSZ sshd[24259]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:21 LabSZ sshd[24261]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:23 LabSZ sshd[24261]: Failed password for root from 112.95.230.3 port 39041 ssh2\n" +
                    "Dec 10 07:28:23 LabSZ sshd[24261]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:23 LabSZ sshd[24263]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:25 LabSZ sshd[24263]: Failed password for root from 112.95.230.3 port 40388 ssh2\n" +
                    "Dec 10 07:28:25 LabSZ sshd[24263]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:25 LabSZ sshd[24265]: Invalid user utsims from 112.95.230.3\n" +
                    "Dec 10 07:28:25 LabSZ sshd[24265]: input_userauth_request: invalid user utsims [preauth]\n" +
                    "Dec 10 07:28:25 LabSZ sshd[24265]: pam_unix(sshd:auth): check pass; user unknown\n" +
                    "Dec 10 07:28:25 LabSZ sshd[24265]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3 \n" +
                    "Dec 10 07:28:28 LabSZ sshd[24265]: Failed password for invalid user utsims from 112.95.230.3 port 41506 ssh2\n" +
                    "Dec 10 07:28:28 LabSZ sshd[24265]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:28 LabSZ sshd[24267]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:30 LabSZ sshd[24267]: Failed password for root from 112.95.230.3 port 42881 ssh2\n" +
                    "Dec 10 07:28:30 LabSZ sshd[24267]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:31 LabSZ sshd[24269]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:33 LabSZ sshd[24269]: Failed password for root from 112.95.230.3 port 43981 ssh2\n" +
                    "Dec 10 07:28:33 LabSZ sshd[24269]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:33 LabSZ sshd[24271]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:35 LabSZ sshd[24271]: Failed password for root from 112.95.230.3 port 44900 ssh2\n" +
                    "Dec 10 07:28:35 LabSZ sshd[24271]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:35 LabSZ sshd[24273]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:37 LabSZ sshd[24273]: Failed password for root from 112.95.230.3 port 45699 ssh2\n" +
                    "Dec 10 07:28:37 LabSZ sshd[24273]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:37 LabSZ sshd[24275]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:39 LabSZ sshd[24275]: Failed password for root from 112.95.230.3 port 46577 ssh2\n" +
                    "Dec 10 07:28:39 LabSZ sshd[24275]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:40 LabSZ sshd[24277]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:42 LabSZ sshd[24277]: Failed password for root from 112.95.230.3 port 47836 ssh2\n" +
                    "Dec 10 07:28:42 LabSZ sshd[24277]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:42 LabSZ sshd[24279]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:44 LabSZ sshd[24279]: Failed password for root from 112.95.230.3 port 49133 ssh2\n" +
                    "Dec 10 07:28:44 LabSZ sshd[24279]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:45 LabSZ sshd[24281]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:46 LabSZ sshd[24281]: Failed password for root from 112.95.230.3 port 50655 ssh2\n" +
                    "Dec 10 07:28:46 LabSZ sshd[24281]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:47 LabSZ sshd[24283]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:49 LabSZ sshd[24283]: Failed password for root from 112.95.230.3 port 51982 ssh2\n" +
                    "Dec 10 07:28:49 LabSZ sshd[24283]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:28:49 LabSZ sshd[24285]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=112.95.230.3  user=root\n" +
                    "Dec 10 07:28:51 LabSZ sshd[24285]: Failed password for root from 112.95.230.3 port 53584 ssh2\n" +
                    "Dec 10 07:28:51 LabSZ sshd[24285]: Received disconnect from 112.95.230.3: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:32:24 LabSZ sshd[24287]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=123.235.32.19  user=root\n" +
                    "Dec 10 07:32:27 LabSZ sshd[24287]: Failed password for root from 123.235.32.19 port 40652 ssh2\n" +
                    "Dec 10 07:32:27 LabSZ sshd[24287]: Received disconnect from 123.235.32.19: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:32:27 LabSZ sshd[24289]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=123.235.32.19  user=root\n" +
                    "Dec 10 07:32:29 LabSZ sshd[24289]: Failed password for root from 123.235.32.19 port 49720 ssh2\n" +
                    "Dec 10 07:32:29 LabSZ sshd[24289]: Received disconnect from 123.235.32.19: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:33:58 LabSZ sshd[24291]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=123.235.32.19  user=root\n" +
                    "Dec 10 07:34:00 LabSZ sshd[24291]: Failed password for root from 123.235.32.19 port 45568 ssh2\n" +
                    "Dec 10 07:34:00 LabSZ sshd[24291]: Received disconnect from 123.235.32.19: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:34:02 LabSZ sshd[24293]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=123.235.32.19  user=root\n" +
                    "Dec 10 07:34:04 LabSZ sshd[24293]: Failed password for root from 123.235.32.19 port 48588 ssh2\n" +
                    "Dec 10 07:34:06 LabSZ sshd[24293]: Received disconnect from 123.235.32.19: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:34:07 LabSZ sshd[24295]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=123.235.32.19  user=root\n" +
                    "Dec 10 07:34:10 LabSZ sshd[24295]: Failed password for root from 123.235.32.19 port 50950 ssh2\n" +
                    "Dec 10 07:34:10 LabSZ sshd[24295]: Received disconnect from 123.235.32.19: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:34:13 LabSZ sshd[24297]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=123.235.32.19  user=root\n" +
                    "Dec 10 07:34:15 LabSZ sshd[24297]: Failed password for root from 123.235.32.19 port 54024 ssh2\n" +
                    "Dec 10 07:34:15 LabSZ sshd[24297]: Received disconnect from 123.235.32.19: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:34:21 LabSZ sshd[24299]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=123.235.32.19  user=root\n" +
                    "Dec 10 07:34:23 LabSZ sshd[24299]: Failed password for root from 123.235.32.19 port 57100 ssh2\n" +
                    "Dec 10 07:34:24 LabSZ sshd[24299]: Received disconnect from 123.235.32.19: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:34:33 LabSZ sshd[24301]: Did not receive identification string from 123.235.32.19\n" +
                    "Dec 10 07:35:15 LabSZ sshd[24303]: Did not receive identification string from 177.79.82.136\n" +
                    "Dec 10 07:42:49 LabSZ sshd[24318]: Invalid user inspur from 183.136.162.51\n" +
                    "Dec 10 07:42:49 LabSZ sshd[24318]: input_userauth_request: invalid user inspur [preauth]\n" +
                    "Dec 10 07:42:49 LabSZ sshd[24318]: pam_unix(sshd:auth): check pass; user unknown\n" +
                    "Dec 10 07:42:49 LabSZ sshd[24318]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=183.136.162.51 \n" +
                    "Dec 10 07:42:51 LabSZ sshd[24318]: Failed password for invalid user inspur from 183.136.162.51 port 55204 ssh2\n" +
                    "Dec 10 07:42:51 LabSZ sshd[24318]: Received disconnect from 183.136.162.51: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:48:00 LabSZ sshd[24321]: reverse mapping checking getaddrinfo for 191-210-223-172.user.vivozap.com.br [191.210.223.172] failed - POSSIBLE BREAK-IN ATTEMPT!\n" +
                    "Dec 10 07:48:00 LabSZ sshd[24321]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=191.210.223.172  user=root\n" +
                    "Dec 10 07:48:03 LabSZ sshd[24321]: Failed password for root from 191.210.223.172 port 31473 ssh2\n" +
                    "Dec 10 07:48:03 LabSZ sshd[24321]: Connection closed by 191.210.223.172 [preauth]\n" +
                    "Dec 10 07:51:09 LabSZ sshd[24323]: Did not receive identification string from 195.154.37.122\n" +
                    "Dec 10 07:51:12 LabSZ sshd[24324]: reverse mapping checking getaddrinfo for 195-154-37-122.rev.poneytelecom.eu [195.154.37.122] failed - POSSIBLE BREAK-IN ATTEMPT!\n" +
                    "Dec 10 07:51:12 LabSZ sshd[24324]: Invalid user support from 195.154.37.122\n" +
                    "Dec 10 07:51:12 LabSZ sshd[24324]: input_userauth_request: invalid user support [preauth]\n" +
                    "Dec 10 07:51:12 LabSZ sshd[24324]: pam_unix(sshd:auth): check pass; user unknown\n" +
                    "Dec 10 07:51:12 LabSZ sshd[24324]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=195.154.37.122 \n" +
                    "Dec 10 07:51:15 LabSZ sshd[24324]: Failed password for invalid user support from 195.154.37.122 port 56539 ssh2\n" +
                    "Dec 10 07:51:15 LabSZ sshd[24324]: error: Received disconnect from 195.154.37.122: 3: com.jcraft.jsch.JSchException: Auth fail [preauth]\n" +
                    "Dec 10 07:51:17 LabSZ sshd[24326]: reverse mapping checking getaddrinfo for 195-154-37-122.rev.poneytelecom.eu [195.154.37.122] failed - POSSIBLE BREAK-IN ATTEMPT!\n" +
                    "Dec 10 07:51:18 LabSZ sshd[24326]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=195.154.37.122  user=uucp\n" +
                    "Dec 10 07:51:20 LabSZ sshd[24326]: Failed password for uucp from 195.154.37.122 port 59266 ssh2\n" +
                    "Dec 10 07:51:20 LabSZ sshd[24326]: error: Received disconnect from 195.154.37.122: 3: com.jcraft.jsch.JSchException: Auth fail [preauth]\n" +
                    "Dec 10 07:53:26 LabSZ sshd[24329]: Connection closed by 194.190.163.22 [preauth]\n" +
                    "Dec 10 07:55:55 LabSZ sshd[24331]: Invalid user test from 52.80.34.196\n" +
                    "Dec 10 07:55:55 LabSZ sshd[24331]: input_userauth_request: invalid user test [preauth]\n" +
                    "Dec 10 07:55:55 LabSZ sshd[24331]: pam_unix(sshd:auth): check pass; user unknown\n" +
                    "Dec 10 07:55:55 LabSZ sshd[24331]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=ec2-52-80-34-196.cn-north-1.compute.amazonaws.com.cn \n" +
                    "Dec 10 07:56:02 LabSZ sshd[24331]: Failed password for invalid user test from 52.80.34.196 port 36060 ssh2\n" +
                    "Dec 10 07:56:02 LabSZ sshd[24331]: Received disconnect from 52.80.34.196: 11: Bye Bye [preauth]\n" +
                    "Dec 10 07:56:13 LabSZ sshd[24333]: Did not receive identification string from 103.207.39.165\n" +
                    "Dec 10 07:56:14 LabSZ sshd[24334]: Invalid user support from 103.207.39.165\n" +
                    "Dec 10 07:56:14 LabSZ sshd[24334]: input_userauth_request: invalid user support [preauth]\n" +
                    "Dec 10 07:56:14 LabSZ sshd[24334]: pam_unix(sshd:auth): check pass; user unknown\n" +
                    "Dec 10 07:56:14 LabSZ sshd[24334]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=103.207.39.165 \n" +
                    "Dec 10 07:56:15 LabSZ sshd[24334]: Failed password for invalid user support from 103.207.39.165 port 58158 ssh2\n" +
                    "Dec 10 07:56:15 LabSZ sshd[24334]: Received disconnect from 103.207.39.165: 11: Closed due to user request. [preauth]\n" +
                    "Dec 10 08:07:00 LabSZ sshd[24336]: Connection closed by 194.190.163.22 [preauth]\n" +
                    "Dec 10 08:08:41 LabSZ sshd[24338]: Invalid user inspur from 175.102.13.6\n" +
                    "Dec 10 08:08:41 LabSZ sshd[24338]: input_userauth_request: invalid user inspur [preauth]\n" +
                    "Dec 10 08:08:41 LabSZ sshd[24338]: pam_unix(sshd:auth): check pass; user unknown\n" +
                    "Dec 10 08:08:41 LabSZ sshd[24338]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=175.102.13.6 \n" +
                    "Dec 10 08:08:43 LabSZ sshd[24338]: Failed password for invalid user inspur from 175.102.13.6 port 47130 ssh2\n" +
                    "Dec 10 08:08:43 LabSZ sshd[24338]: Received disconnect from 175.102.13.6: 11: Bye Bye [preauth]\n" +
                    "Dec 10 08:20:23 LabSZ sshd[24358]: Connection closed by 194.190.163.22 [preauth]\n" +
                    "Dec 10 08:24:32 LabSZ sshd[24361]: Invalid user  0101 from 5.188.10.180\n" +
                    "Dec 10 08:24:32 LabSZ sshd[24361]: input_userauth_request: invalid user  0101 [preauth]\n" +
                    "Dec 10 08:24:32 LabSZ sshd[24361]: pam_unix(sshd:auth): check pass; user unknown\n" +
                    "Dec 10 08:24:32 LabSZ sshd[24361]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=5.188.10.180 \n" +
                    "Dec 10 08:24:35 LabSZ sshd[24361]: Failed password for invalid user  0101 from 5.188.10.180 port 36279 ssh2\n" +
                    "Dec 10 08:24:36 LabSZ sshd[24361]: Connection closed by 5.188.10.180 [preauth]\n" +
                    "Dec 10 08:24:40 LabSZ sshd[24363]: Invalid user 0 from 5.188.10.180\n" +
                    "Dec 10 08:24:40 LabSZ sshd[24363]: input_userauth_request: invalid user 0 [preauth]\n" +
                    "Dec 10 08:24:40 LabSZ sshd[24363]: Failed none for invalid user 0 from 5.188.10.180 port 49811 ssh2\n" +
                    "Dec 10 08:24:43 LabSZ sshd[24363]: pam_unix(sshd:auth): check pass; user unknown\n" +
                    "Dec 10 08:24:43 LabSZ sshd[24363]: pam_unix(sshd:auth): authentication failure; logname= uid=0 euid=0 tty=ssh ruser= rhost=5.188.10.180 \n" +
                    "Dec 10 08:24:45 LabSZ sshd[24363]: Failed password for invalid user 0 from 5.188.10.180 port 49811 ssh2\n" +
                    "Dec 10 08:24:46 LabSZ sshd[24363]: Connection closed by 5.188.10.180 [preauth]\n" +
                    "Dec 10 08:24:50 LabSZ sshd[24365]: Invalid user 1234 from 5.188.10.180\n" +
                    "Dec 10 08:24:50 LabSZ sshd[24365]: input_userauth_request: invalid user 1234 [preauth]\n" +
                    "Dec 10 08:24:50 LabSZ sshd[24365]: pam_unix(sshd:auth): check pass; user unknown";

    public static void main(String[] args) {
        System.out.println("Hello world!");

        String deploymentName = "log-analysis-deployment";

        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(AZURE_OPENAI_LANGUAGE_ENDPOINT)
                .credential(new AzureKeyCredential(AZURE_OPENAI_API_KEY))
                .buildClient();

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, "You are a helpful assistant"));
        chatMessages.add(new ChatMessage(ChatRole.USER, "Could you tell me what was the user \"Bye Bye\" based on these logs? \n" + LOG_EXAMPLE));
//        chatMessages.add(new ChatMessage(ChatRole.ASSISTANT, "Yes, customer managed keys are supported by Azure OpenAI?"));
//        chatMessages.add(new ChatMessage(ChatRole.USER, "Do other Azure AI services support this too?"));


        ChatCompletions chatCompletions = client.getChatCompletions(deploymentName, new ChatCompletionsOptions(chatMessages));

        System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
        }

        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
    }
}
