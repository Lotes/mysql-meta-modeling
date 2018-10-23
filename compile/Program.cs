﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace compile
{
    public class Program
    {
        private static Regex Pattern = new Regex("^(--|#)import \"([^\"]+)\"\\s*$", RegexOptions.Multiline);
        private static Regex Separator = new Regex("(/|\\\\)", RegexOptions.Multiline);

        static void Main(string[] args)
        {
            if (args.Length == 0)
                return;
            var connectionStringBuilder = new MySql.Data.MySqlClient.MySqlConnectionStringBuilder();
            connectionStringBuilder.SslMode = MySql.Data.MySqlClient.MySqlSslMode.None;
            connectionStringBuilder.Port = 3306;
            connectionStringBuilder.UserID = "root";
            connectionStringBuilder.Password = "123456";
            connectionStringBuilder.AllowUserVariables = true;
            using (var connection = new MySql.Data.MySqlClient.MySqlConnection(connectionStringBuilder.ToString()))
            {
                connection.Open();

                using (var cmd = connection.CreateCommand())
                {
                    cmd.CommandText = "DROP DATABASE IF EXISTS meta_como; CREATE DATABASE meta_como; USE meta_como;";
                    cmd.ExecuteNonQuery();
                }

                var fileName = args[0];
                var info = new FileInfo(fileName);
                var files = new HashSet<string>();
                var stack = new Stack<string>();

                using (var transaction = connection.BeginTransaction())
                {
                    ProcessFile(info, files, stack, file =>
                    {
                        using (var cmd = connection.CreateCommand())
                        {
                            cmd.Transaction = transaction;
                            cmd.CommandText = File.ReadAllText(file.FullName);
                            try
                            {
                                cmd.ExecuteNonQuery();
                            }
                            catch (MySql.Data.MySqlClient.MySqlException ex)
                            {
                                Console.WriteLine("CALL " + file.FullName.Substring(info.Directory.FullName.Length + 1));
                                Console.Error.WriteLine("ERROR: " + ex.Message);
                            }
                        }
                    });
                    transaction.Commit();
                }
            }
        }

        private static void ProcessFile(FileInfo info, HashSet<string> files, Stack<string> stack, Action<FileInfo> doIt)
        {
            if (files.Contains(info.FullName))
                return; //already read
            if (stack.Contains(info.FullName))
                throw new InvalidOperationException("Cycle detected: "+string.Join("->", stack));
            files.Add(info.FullName);
            stack.Push(info.FullName);
            var content = File.ReadAllText(info.FullName);
            foreach(Match match in Pattern.Matches(content))
            {
                var directory = info.Directory.FullName;
                var fileName = Separator.Replace(match.Groups[2].Value, Path.DirectorySeparatorChar.ToString());
                var nextInfo = new FileInfo(Path.Combine(directory, fileName));
                ProcessFile(nextInfo, files, stack, doIt);
            }
            doIt(info);
        }
    }
}
