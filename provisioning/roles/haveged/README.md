# haveged

This role configures [haveged](http://www.issihosts.com/haveged/), an entropy daemon. It feeds Linux's random number generator additional
randomness based on CPU flutter. This is important because servers on cloud providers, like ours, tend to run low on entropy because they
don't have many good sources of it. When Linux runs low on entropy, generating random numbers becomes really slow. That's a Bad Thing
because we use random numbers for a bunch of things, like generating password reset codes or session IDs.

Since we're using CoreOS, haveged runs in a privileged Docker container, which gives it access to the host's entropy device.

You can check how much entropy is available by running `cat /proc/sys/kernel/random/entropy_avail` This should be at least a few hundred. If not,
check the haveged logs (`sudo journalctl -u haveged` and/or `docker logs haveged`).