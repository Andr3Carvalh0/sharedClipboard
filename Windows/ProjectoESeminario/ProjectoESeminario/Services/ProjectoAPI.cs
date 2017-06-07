﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Projecto.Service
{

    public class ProjectoAPI : IAPI
    {
        /// <summary>
        /// Server location
        /// </summary>
        private readonly static String mainServer = "http://projecto1617.herokuapp.com/api/";

        /// <summary>
        /// URI for the textual push
        /// </summary>
        private readonly static String push = "push";

        /// <summary>
        /// URI for the MIME push
        /// </summary>
        private readonly static String pushMIME = "pushMIME";

        /// <summary>
        /// URI for pulling data
        /// </summary>
        private readonly static String pull = "pull";

        /// <summary>
        /// URI used to create/authenticate user
        /// </summary>
        private readonly static String accountManagement = "account";

        /// <summary>
        /// Object to do the HTTP requests
        /// </summary>
        private readonly HttpClient httpClient;

        public ProjectoAPI() {
            this.httpClient = new HttpClient();
        }

        public async Task<HttpResponseMessage> Authenticate(string username, string password){
            return await httpClient.GetAsync(mainServer + accountManagement + "?account=" + username + "&password=" + password);
        }

        public async Task<HttpResponseMessage> CreateAccount(string username, string password)
        {
            var parameters = new Dictionary<string, string>();
            parameters["account"] = username;
            parameters["password"] = password;
            return await httpClient.PutAsync(mainServer + accountManagement, new FormUrlEncodedContent(parameters));
        }

        public async Task<HttpResponseMessage> Pull(long account)
        {
            if (account == 0)
                return null;

            return await httpClient.GetAsync(mainServer + pull + "?account=" + account);
        }

        public async Task<HttpResponseMessage> Push(long account, string data)
        {
            if (account == 0)
                return null;

            var parameters = new Dictionary<string, string>();
            parameters["token"] = account + "";
            parameters["data"] = data;
            return await httpClient.PutAsync(mainServer + push, new FormUrlEncodedContent(parameters));

        }

        public async Task<HttpResponseMessage> Push(long account, MemoryStream data)
        {
            if (account == 0)
                return null;

            MultipartFormDataContent content = new MultipartFormDataContent();

            content.Add(new StreamContent(data));
            
            var parameters = new Dictionary<object, object>();
            parameters["token"] = account;
            parameters["file"] = content;
            return await httpClient.PostAsync(mainServer + pushMIME, content);

        }
    }
}
