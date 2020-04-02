FROM alpine
MAINTAINER Zulu Enterprise Container Images <azul-zulu-images@microsoft.com>

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

ARG ZULU_DIR=zulu-8-azure-jre_8.44.0.11-8.0.242-linux_musl_x64

RUN ZULU_PACK=${ZULU_DIR}.tar.gz && \
    INSTALL_DIR=/usr/lib/jvm && \
    BIN_DIR=/usr/bin && \
    MAN_DIR=/usr/share/man/man1 && \
    apk --no-cache add ca-certificates libgcc libstdc++ ttf-dejavu wget && \
    apk update && \
    apk upgrade && \
    wget -q http://repos.azul.com/azure-only/zulu/packages/zulu-8/8u242/$ZULU_PACK && \
    mkdir -p ${INSTALL_DIR} && \
    tar -xf ./${ZULU_PACK} -C ${INSTALL_DIR} && rm -f ${ZULU_PACK} && \
    cd ${BIN_DIR} && \
    find ${INSTALL_DIR}/${ZULU_DIR}/bin -type f -perm -a=x -exec ln -s {} . \; && \
    mkdir -p ${MAN_DIR} && \
    cd ${MAN_DIR} && \
    find ${INSTALL_DIR}/${ZULU_DIR}/man/man1 -type f -name "*.1" -exec ln -s {} . \;

ENV JAVA_HOME=/usr/lib/jvm/${ZULU_DIR}

COPY . .
CMD echo "println(2)" | java -cp "./target/xitrum/lib/*" com.xiningli.scalapractice.Hi


#CMD java -cp in-men-jre-compiler-test.jar in.men.jre.compiler.App