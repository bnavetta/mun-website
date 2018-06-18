#!/usr/bin/bash

# Bootstraps the system for Ansible, which mostly involves setting up Python

mkdir -p /opt/local

PYPY_VERSION="3.5-6.0.0-linux_x86_64"
wget "https://bitbucket.org/squeaky/portable-pypy/downloads/pypy${PYPY_VERSION}-portable.tar.bz2"
tar xf "pypy${PYPY_VERSION}-portable.tar.bz2" --strip-components=1 -C /opt/local
